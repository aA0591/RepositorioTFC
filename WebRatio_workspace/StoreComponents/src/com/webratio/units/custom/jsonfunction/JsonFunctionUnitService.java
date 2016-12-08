package com.webratio.units.custom.jsonfunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.webratio.rtx.RTXConstants;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.RTXOperationUnitService;
import com.webratio.rtx.beans.ExtendedOperationUnitBean;
import com.webratio.rtx.core.AbstractService;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.rtx.core.DescriptorHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JsonFunctionUnitService extends AbstractService implements RTXOperationUnitService {

	private static final String MODE_CODE = "codeJson";
	private static final String MODE_DECODE = "decodeJson";
	private static final String MODE_REPLACE = "replaceTextWithJson";

	/** The unit mode, indicating the performed operation */
	private final String mode;

	/** The string list (each one is related to a subunit) */
	private final List keys;

	/** The string list (each one is related to a subunit (Names)) */
	private final List names;

	/**
	 * Constructs a new Json Function Component service
	 * 
	 * @param id
	 *            the id of the unit.
	 * @param mgr
	 *            the shared runtime manager.
	 * @param descr
	 *            the unit descriptor element.
	 * @throws RTXException
	 *             in case an error occurs initializing the service.
	 */
	public JsonFunctionUnitService(String id, RTXManager mgr, Element descr) throws RTXException {
		super(id, mgr, descr);
		this.mode = DescriptorHelper.getChildValue(descr, "Mode", true, this);
		this.keys = DescriptorHelper.getChildren(descr, "Key", false, this);
		this.names = DescriptorHelper.getChildren(descr, "Name", false, this);
		// TODO Auto-generated constructor stub
	}

	public Object execute(Map operationContext, Map sessionContext)	throws RTXException {
		// TODO Auto-generated method stub
		ExtendedOperationUnitBean bean = new ExtendedOperationUnitBean();
		try {
			if (MODE_CODE.equals(mode)) {
				doCodeJson(bean, operationContext, sessionContext);
			} else if (MODE_DECODE.equals(mode)) {
				doDecodeJson(bean, operationContext, sessionContext);
			} else if (MODE_REPLACE.equals(mode)) {
				doReplaceTextWithJson(bean, operationContext, sessionContext);
			}

		} catch (Exception e) {
			logError("Unable to execute the function unit service", e);
			bean.setResultCode(RTXConstants.ERROR_CODE);
			bean.put("errorMessage", e.getMessage());
			bean.put("exception", e);
			throw new RTXException(e);
		}
		return bean;

	}

	/**
	 * Code two or more keys into json value.
	 * 
	 * @param bean
	 *            the result unit bean.
	 * @param operationContext
	 *            a set of name-to-object bindings, preloaded with values of
	 *            parameters (having scope = request).
	 * @param sessionContext
	 *            a set of name-to-object bindings, preloaded with values of
	 *            parameters (having scope = session).
	 * @throws Exception
	 */
	private void doCodeJson(ExtendedOperationUnitBean bean,
			Map operationContext, Map sessionContext) throws Exception {

		JSONObject obj = new JSONObject();
		Iterator iteratorNames = names.iterator();
		Iterator iteratorKey = keys.iterator();
		Integer i = 0;

		while (iteratorNames.hasNext() && iteratorKey.hasNext()) {
			i++;
			Element stringElemN = (Element) iteratorNames.next();
			Element stringElemK = (Element) iteratorKey.next();

			Object name = stringElemN.valueOf("@name");
			Object value = stringElemK.valueOf("@value");

			String key = stringElemK.getText();
			if (StringUtils.isEmpty((String) value)) {
				value = BeanHelper.asString(operationContext.get(key));
			}
			// In case that value is null
			if (value == null) {
				value = "";
			}
			// In case that name is blank
			if (name == "") {
				name = "KeyParameter" + i;
			}
			obj.put(name, value);
		}
		bean.put("resultJson", obj);
	}

	/**
	 * De-code a single string into json parameter.
	 * 
	 * @param bean
	 *            the result unit bean.
	 * @param operationContext
	 *            a set of name-to-object bindings, preloaded with values of
	 *            parameters (having scope = request).
	 * @param sessionContext
	 *            a set of name-to-object bindings, preloaded with values of
	 *            parameters (having scope = session).
	 * @throws Exception
	 */
	private void doDecodeJson(ExtendedOperationUnitBean bean, Map operationContext, Map sessionContext) throws Exception {
		List<Object> keyValues = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();

		String inputString = BeanHelper.asString(operationContext.get(getId() + ".jsonString"));
		String inputKey = BeanHelper.asString(operationContext.get(getId() + ".keyParameter"));
		// Code inputString for Array		
		JSONObject obj2 = (JSONObject) JSONValue.parse(inputString);

		if (inputKey == null) {
			Iterator<String> iterator = obj2.keySet().iterator();
			while (iterator.hasNext()) {
				keyValues.add(iterator.next());
			}
			for (int i = 0; i < keyValues.size(); i++) {
				values.add(obj2.get(keyValues.get(i)));
			}
			bean.put("resultDecodeJsonKeys", keyValues);
			bean.put("resultDecodeJsonValues", values);
		} else {
			bean.put("resultDecodeJsonKeys", inputKey);
			bean.put("resultDecodeJsonValues", obj2.get(inputKey));
		}
	}

	/**
	 * Replace values in text with keys and value of Json String.
	 * 
	 * @param bean
	 *            the result unit bean.
	 * @param operationContext
	 *            a set of name-to-object bindings, preloaded with values of
	 *            parameters (having scope = request).
	 * @param sessionContext
	 *            a set of name-to-object bindings, preloaded with values of
	 *            parameters (having scope = session).
	 * @throws Exception
	 */
	private void doReplaceTextWithJson(ExtendedOperationUnitBean bean,	Map operationContext, Map sessionContext) throws Exception {
		String inputStringJson = BeanHelper.asString(operationContext.get(getId() + ".jsonString"));
		String inputText = BeanHelper.asString(operationContext.get(getId() + ".textValue"));
		String iteratorTemp;
		// Code inputString for Array
		try {			
			JSONObject obj2 = (JSONObject) JSONValue.parse(inputStringJson);

			Iterator<String> iterator = obj2.keySet().iterator();
			while (iterator.hasNext()) {
				iteratorTemp = iterator.next();
				inputText = inputText.replace("#" + iteratorTemp + "#",	(String) obj2.get(iteratorTemp));
			}
			bean.put("resultText", inputText);
			
		} catch (Exception e) {
			// TODO: handle exception
			bean.put("resultText", inputText);
		}

	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

}