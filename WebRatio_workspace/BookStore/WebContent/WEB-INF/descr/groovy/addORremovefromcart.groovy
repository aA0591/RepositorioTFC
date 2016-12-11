#input int value, String operation

import com.webratio.rtx.core.BeanHelper


def newvalue
operationf = BeanHelper.asString(operation)

if(operationf.equals("add")){
	newvalue = value + 1
}
else{
	newvalue = value - 1
}


if(newvalue > 0){
	return newvalue
}
else{
	return null
}


