#input String[] oldauthors, String inputauthors, int[] indexes
#output String[] newauthorsList, int[] newindexes, int beforesize, int newsize

findcharacter = inputauthors.indexOf(",")
def newauthorsList = []
def newindexes = []
def newsize

	if(findcharacter != -1){
		inputauthors = inputauthors.split(",")
		for(i = 0 ; i < inputauthors.size(); i++){
			inputauthors[i] = inputauthors[i].trim()
			reg = -1
			for(j= 0; j < oldauthors.size(); j++){
				if(inputauthors[i].equals(oldauthors[j])){
					reg = 0
					newindexes.add(indexes[j])
				}
			}
			if(reg != 0){
				newauthorsList.add(inputauthors[i])
			}
		}
	}
	else{
			reg = -1
			for(j= 0; j < oldauthors.size(); j++){
				if(inputauthors.equals(oldauthors[j])){
					reg = 0
					newindexes.add(indexes[j])
				}
			}
			if(reg != 0){
				newauthorsList.add(inputauthors)
			}
	}
	
	beforesize = newindexes.size()
	newsize = newauthorsList.size()

	
	return ["newauthorsList":newauthorsList, "newindexes":newindexes, "beforesize":beforesize, "newsize":newsize];