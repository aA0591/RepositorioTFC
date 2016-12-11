#input int[] oldauthors, int[] newauthors
#output int[] newindexes

def newindexes = []
for(i = 0 ; i<oldauthors.size();i++ ){
	newindexes.add(oldauthors[i])
}

for(i = 0 ; i<newauthors.size();i++ ){
	newindexes.add(newauthors[i])
}

return ["newindexes":newindexes];