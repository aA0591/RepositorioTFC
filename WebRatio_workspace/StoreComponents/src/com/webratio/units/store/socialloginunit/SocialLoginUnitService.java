package com.webratio.units.store.socialloginunit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.dom4j.Element;

import com.webratio.rtx.RTXCachedContentUnitService;
import com.webratio.rtx.RTXConstants;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.RTXOperationUnitService;
import com.webratio.rtx.beans.ExtendedContentUnitBean;
import com.webratio.rtx.beans.ExtendedOperationUnitBean;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.rtx.core.DescriptorHelper;
import com.webratio.rtx.db.AbstractDBService;
import com.webratio.struts.HttpRequestHelper;
import com.webratio.struts.WRGlobals;
import com.webratio.units.store.commons.application.ISocialNetworkService;
import com.webratio.units.store.commons.auth.AccessToken;
import com.webratio.units.store.commons.auth.InvalidAccessTokenException;
import com.webratio.units.store.social.SocialNetworkServiceProvider;

/**
 * The runtime service for social units.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SocialLoginUnitService extends AbstractDBService implements RTXCachedContentUnitService, RTXOperationUnitService {

    private Map socialNetworks;
    private boolean performLogout;

    public SocialLoginUnitService(String id, RTXManager mgr, Element descr) throws RTXException {
        super(id, mgr, descr);

        /* retrieves the social network services */
        String[] socialNetworkIds = StringUtils.split(DescriptorHelper.getChildValue(descr, "SocialNetworks", true, this), "|");
        Map temp = new HashMap();
        for (Iterator iterator = SocialNetworkServiceProvider.provide(socialNetworkIds, getManager()).iterator(); iterator.hasNext();) {
            ISocialNetworkService sns = (ISocialNetworkService) iterator.next();
            temp.put(sns.getId(), sns);
        }
        socialNetworks = Collections.unmodifiableMap(temp);
        performLogout = new Boolean(StringUtils.defaultIfEmpty(DescriptorHelper.getChildValue(descr, "PerformLogout", false, this),
                "false")).booleanValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXContentUnitService#computeParameterValue(java.lang.String, java.util.Map, java.util.Map)
     */
    public Object computeParameterValue(String outputParamName, Map localContext, Map sessionContext) throws RTXException {
        Object unitBean = getUnitBean(localContext, sessionContext);
        if (unitBean == null) {
            return null;
        }
        return BeanHelper.getBeanProperty(unitBean, outputParamName, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXContentUnitService#execute(java.util.Map, java.util.Map)
     */
    public Object execute(Map localContext, Map sessionContext) throws RTXException {
        return getUnitBean(localContext, sessionContext);
    }

    /**
     * Gets (or recomputes) the unit bean.
     * 
     * @param localContext
     *            a set of name-to-object bindings, preloaded with values of parameters (having scope = page).
     * @param sessionContext
     *            a set of name-to-object bindings, preloaded with values of parameters (having scope = session).
     * @return the unit bean.
     * @throws RTXException
     *             if an error occurred computing the bean.
     */
    protected Object getUnitBean(Map localContext, Map sessionContext) throws RTXException {
        Object unitBean = localContext.get('_' + getId());
        if (unitBean == null || Boolean.TRUE.equals(localContext.get(RTXConstants.IN_OPERATION_KEY))) {
            unitBean = createUnitBean(localContext, sessionContext);
        }
        localContext.put('_' + getId(), unitBean);
        return unitBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXCachedContentUnitService#createUnitBean(java.util.Map, java.util.Map)
     */
    public Object createUnitBean(Map localContext, Map sessionContext) throws RTXException {
        ExtendedOperationUnitBean bean = new ExtendedOperationUnitBean();
        try {

            /* computes the callback URL */
            HttpServletRequest request = (HttpServletRequest) localContext.get(RTXConstants.HTTP_SERVLET_REQUEST_KEY);
            ServletContext servletContext = (ServletContext) getManager().getApplicationContext().get(RTXConstants.SERVLET_CONTEXT);
            HttpRequestHelper httpReqHelper = (HttpRequestHelper) servletContext.getAttribute(WRGlobals.HTTP_REQUEST_HELPER_KEY);
            String callbackUrl = httpReqHelper.getAbsoluteURLContext(servletContext, request) + "/";
            String urlExt = StringUtils.defaultIfEmpty((String) servletContext.getAttribute(WRGlobals.URL_EXT_KEY), ".do");

            /* executes as operation unit */
            if (Boolean.TRUE.equals(localContext.get(RTXConstants.IN_OPERATION_KEY))) {

                /* performs logout if requested */
                String socialNetworkId = BeanHelper.asString(localContext.get(getId() + ".socialNetwork"));
                if (performLogout) {
                    logout((ISocialNetworkService) getManager().getService(socialNetworkId), localContext, sessionContext);
                } else {
                    callbackUrl += StringUtils.defaultIfEmpty(getManager().getModelService().getURLName(getId()), getId()) + urlExt;

                    /* initializes access token socialNetworkId is blank if user landing back from external authentication page */
                    if (StringUtils.isBlank(socialNetworkId)) {
                        socialNetworkId = BeanHelper.asString(sessionContext.remove(getId() + ".socialNetwork"));
                        ISocialNetworkService socialNetwork = (ISocialNetworkService) socialNetworks.get(socialNetworkId);
                        if (socialNetwork != null) {
                            String accessToken = initAccessToken(socialNetwork, localContext, sessionContext);
                            bean.put("accessToken", accessToken);
                            if (socialNetwork.isAuthorized(localContext, sessionContext)) {
                                bean.put("user", socialNetwork.getUser(localContext, sessionContext));
                                bean.setResultCode(RTXConstants.SUCCESS_CODE);
                                return bean;
                            }
                        }
                        bean.setErrorMessage("Missing input socialNetwork identifier parameter");
                        bean.setResultCode(RTXConstants.ERROR_CODE);
                        return bean;
                    }

                    /* redirects the client to the authentication page */
                    ISocialNetworkService socialNetwork = (ISocialNetworkService) getManager().getService(socialNetworkId);
                    sessionContext.put(getId() + ".socialNetwork", socialNetwork.getId());
                    String authURL = socialNetwork.computeAuthorizationUrl(callbackUrl, localContext, sessionContext);
                    bean.setResultCode(RTXConstants.REDIRECT_CODE_PREFIX + authURL);
                }
                return bean;
            } else {
                List data = new ArrayList();
                String targetId = (String) localContext.get(RTXConstants.PAGE_ID_KEY);
                callbackUrl += StringUtils.defaultIfEmpty(getManager().getModelService().getURLName(targetId), targetId) + urlExt;

                /* performs logout if requested */
                String logoutSN = request.getParameter("logout");
                if (!StringUtils.isBlank(logoutSN)) {
                    logout((ISocialNetworkService) getManager().getService(logoutSN), localContext, sessionContext);
                }

                /* performs social networks authorization */
                for (Iterator iterator = socialNetworks.values().iterator(); iterator.hasNext();) {
                    ISocialNetworkService socialNetwork = (ISocialNetworkService) iterator.next();
                    ExtendedContentUnitBean serviceBean = new ExtendedContentUnitBean();
                    data.add(serviceBean);
                    serviceBean.put("id", socialNetwork.getId());
                    serviceBean.put("name", socialNetwork.getName());
                    serviceBean.put("homePage", socialNetwork.getHomePage());
                    try {
                        if (!socialNetwork.isAuthorized(localContext, sessionContext)) {
                            initAccessToken(socialNetwork, localContext, sessionContext);
                        }
                        if (socialNetwork.isAuthorized(localContext, sessionContext)) {
                            serviceBean.put("user", socialNetwork.getUser(localContext, sessionContext));
                            serviceBean.put("authorized", Boolean.TRUE);
                        } else {
                            serviceBean.put("authorizeURL",
                                    socialNetwork.computeAuthorizationUrl(callbackUrl, localContext, sessionContext));
                            serviceBean.put("authorized", Boolean.FALSE);
                        }
                    } catch (Throwable e) {
                        if (ExceptionUtils.indexOfThrowable(e, InvalidAccessTokenException.class) > -1) {
                            serviceBean.put("authorizeURL",
                                    socialNetwork.computeAuthorizationUrl(callbackUrl, localContext, sessionContext));
                            serviceBean.put("authorized", Boolean.FALSE);
                        } else {
                            logError("Unable to compute data for social-network " + e, e);
                            serviceBean.put("errorMessage", "Unable to contact " + socialNetwork.getName());
                            serviceBean.put("errorException", e);
                            serviceBean.put("authorized", Boolean.FALSE);
                        }
                    }
                }
                bean.put("data", data.toArray());
            }
        } catch (Exception e) {
            logError("Unable to execute the social unit service", e);
            if (Boolean.TRUE.equals(localContext.get(RTXConstants.IN_OPERATION_KEY))) {
                return new ExtendedOperationUnitBean(false);
            } else if (e instanceof RTXException) {
                throw (RTXException) e;
            } else { // content unit
                throw new RTXException(e);
            }
        }
        return bean;
    }

    private String initAccessToken(ISocialNetworkService socialNetworkService, Map localContext, Map sessionContext)
            throws RTXException {
        HttpServletRequest request = (HttpServletRequest) localContext.get(RTXConstants.HTTP_SERVLET_REQUEST_KEY);
        HttpServletResponse response = (HttpServletResponse) localContext.get(RTXConstants.HTTP_SERVLET_RESPONSE_KEY);
        if (!socialNetworkService.isAuthorized(localContext, sessionContext)) {
            AccessToken accessToken = AccessToken.loadFromCookie(socialNetworkService, request);
            if (accessToken == null) {
                accessToken = socialNetworkService.getAccessToken(localContext, sessionContext);
                if (accessToken != null) {
                    accessToken.storeToCookie(request, response);
                }
            } else {
                socialNetworkService.init(accessToken, localContext, sessionContext);
            }
            if (accessToken != null) {
                return accessToken.getValue();
            }
        }
        AccessToken accessToken = AccessToken.loadFromCookie(socialNetworkService, request);
        if (accessToken == null) {
            return null;
        }
        return accessToken.getValue();
    }

    private void logout(ISocialNetworkService socialNetworkService, Map localContext, Map sessionContext) {
        HttpServletRequest request = (HttpServletRequest) localContext.get(RTXConstants.HTTP_SERVLET_REQUEST_KEY);
        HttpServletResponse response = (HttpServletResponse) localContext.get(RTXConstants.HTTP_SERVLET_RESPONSE_KEY);
        try {
            socialNetworkService.logout(localContext, sessionContext);
        } catch (Exception e) {
            logError("Unable to perform the logout operation", e);
        } finally {
            try {
                AccessToken.invalidate(socialNetworkService, request, response);
            } catch (Exception e2) {
                logError("Unable to invalidate the access token", e2);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXService#dispose()
     */
    public void dispose() {
    }

}
