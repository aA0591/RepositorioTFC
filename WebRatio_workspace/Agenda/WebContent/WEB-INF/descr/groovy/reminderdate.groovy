#input String reminderdate
#output String Day, String Month, String Year

reminderdate = reminderdate.split("-")
return ["Day":reminderdate[2], "Month":reminderdate[1], "Year":reminderdate[0]] 
