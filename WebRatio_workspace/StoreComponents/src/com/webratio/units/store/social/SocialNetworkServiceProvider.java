package com.webratio.units.store.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.webratio.commons.lang.ClassHelper;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.units.store.commons.application.ISocialNetworkService;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SocialNetworkServiceProvider {

    public static List provide(String[] socialNetworkIds, RTXManager rtx) throws RTXException {
        List socialNetworks = new ArrayList();
        for (int i = 0; i < socialNetworkIds.length; i++) {
            Map dataProviderInfo = rtx.getModelService().getServiceDataProviderInfo(socialNetworkIds[i]);
            String name = BeanHelper.asString(dataProviderInfo.get("name"));
            name = StringUtils.replace(name, "+", "Plus");
            String serviceClass = BeanHelper.asString(dataProviderInfo.get("serviceClass"));
            if (StringUtils.isBlank(serviceClass)) {
                serviceClass = SocialNetworkServiceProvider.class.getPackage().getName() + "." + ClassHelper.toValidClassName(name)
                        + "Service";
            }
            try {
                socialNetworks.add((ISocialNetworkService) rtx.getService(socialNetworkIds[i], Class.forName(serviceClass)));
            } catch (ClassNotFoundException e) {
                throw new RTXException("Unable to retrieve service for social network '" + name + "'", e);
            }
        }
        return socialNetworks;
    }

}
