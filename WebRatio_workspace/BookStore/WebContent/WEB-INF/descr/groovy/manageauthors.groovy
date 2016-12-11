#input String inputauthors
#output String[] newauthorsList

findcharacter = inputauthors.indexOf(",")
def newauthorsList = []

if(findcharacter != -1){
		inputauthors = inputauthors.split(",")
		
	for(i = 0 ; i < inputauthors.size(); i++){
		inputauthors[i] = inputauthors[i].trim()
		newauthorsList.add(inputauthors[i])
	}
}
else{
	inputauthors = inputauthors.trim()
	newauthorsList.add(inputauthors)
}


for(i = 0 ; i < newauthorsList.size(); i++){
	System.out.println(" : " + newauthorsList[i])
}

return ["newauthorsList":newauthorsList]