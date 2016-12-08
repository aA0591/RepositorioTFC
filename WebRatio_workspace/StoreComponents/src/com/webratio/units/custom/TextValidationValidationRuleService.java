package com.webratio.units.custom;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.rtx.core.DescriptorHelper;
import com.webratio.rtx.validation.AbstractFieldValidationRuleService;
import com.webratio.rtx.validation.NextValidationPolicy;
import com.webratio.rtx.validation.RTXField;

public class TextValidationValidationRuleService extends AbstractFieldValidationRuleService {

    private static final String TEXT_REGEX = "^[a-zA-Z‡Ž’—œçƒêîò–„ ]*$";
    private static final String NUMERIC_REGEX = "^[0-9]*$";
    private static final String ALPHANUMERIC_REGEX = "^[a-zA-Z‡Ž’—œçƒêîò–„0-9 ]*$";
    private static final String SPECIALCHARS_REGEX = "^[a-zA-Z0-9‡Ž’—œçƒêîò–„ .&-]*$";
    private static final String PASSWORD_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    
    private String validateAs;
    
    public TextValidationValidationRuleService(String id, RTXManager mgr, Element descr) throws RTXException {
        super(id, mgr, descr);
        this.validateAs = DescriptorHelper.getChildValue(descr, "validateAs", true, this);
    }
    
    public NextValidationPolicy validate(RTXField field) throws RTXException {
    	String value = BeanHelper.asString(field.getValue());
    	
    	if (!StringUtils.isEmpty(value)) {
            value = value.trim();

            if (!isValidValue(value, this.validateAs)) {
            	if(this.validateAs.equalsIgnoreCase("TEXT")) {
            		field.addError("textValidation.errorText");
                }else if(this.validateAs.equalsIgnoreCase("NUMERIC")) {
                	field.addError("textValidation.errorNumericOnly");
                }else if(this.validateAs.equalsIgnoreCase("ALPHA")) {
                	field.addError("textValidation.errorAlpha");
                }else if(this.validateAs.equalsIgnoreCase("SPECIAL")) {
                	field.addError("textValidation.errorSpecialChars");
                }else if(this.validateAs.equalsIgnoreCase("PASSWORD")) {
                	field.addError("textValidation.errorPassword");
                }else{
                	field.addError(getBundleKey());
                }
            	
                return NextValidationPolicy.SKIP;
            }

            field.setValue(value);
        }
        return NextValidationPolicy.CONTINUE;
    }

    private static boolean isValidValue(String stringToEvaluate, String validationType) {
        if (stringToEvaluate == null) {
            return false;
        }

        String regexToUse;

        if(validationType.equalsIgnoreCase("NUMERIC")) { 
        	regexToUse = NUMERIC_REGEX;
        }else if(validationType.equalsIgnoreCase("ALPHA")) {	
        	regexToUse = ALPHANUMERIC_REGEX;
        }else if (validationType.equalsIgnoreCase("SPECIAL")){
        	regexToUse = SPECIALCHARS_REGEX;
        }else if(validationType.equalsIgnoreCase("PASSWORD")) {	
        	regexToUse = PASSWORD_REGEX;
        }else{
        	regexToUse = TEXT_REGEX;
        }

        if (!stringToEvaluate.matches(regexToUse)) {
            return false;
        }

        return true;
    }
}