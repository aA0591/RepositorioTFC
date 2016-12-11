#input String timein

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

df1 = new SimpleDateFormat("yyyy-MM-dd")


now = Calendar.instance
date = now.time
millisnow = date.time
println 'millis: ' + millisnow

Date dates = df1.parse(timein)
System.out.println("ingresado: "+ dates)
millisinput = dates.getTime()
println 'millis2: ' + millisinput

diferencia = millisnow - millisinput
days = Math.floor(diferencia / (1000 * 60 * 60 * 24))
System.out.println("days : "+days)

if(days > 365 * 18){
	return 0
}
else{
	return null
}


