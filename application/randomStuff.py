


dictionary = set()
# sum of words
S = 0
# number of documents
M = 0
# number of symbols?
I = 0


# Longest word: link to some image:
"""
http://www.google.co.uk/imgres?imgurl=http://www.lesmills.co.nz/pt_photos/Chris%2520E1.jpg&imgrefurl=http://www.lesmills.co.nz/lesmills_custom.cfm%3F%26do%3Daction%26action%3Dchoose%2520PT%26clubid%3D1%2520OR%2520clubid%2520%3D%25202%2520OR%2520clubid%3D%25203%2520OR%2520clubid%2520%3D%25204%2520OR%2520clubid%2520%3D%25205%2520OR%2520clubid%2520%3D%25206%2520OR%2520clubid%2520%3D%25207%2520OR%2520clubid%2520%3D%25208%2520OR%2520clubid%2520%3D%25209%2520OR%2520clubid%2520%3D%252010&h=448&w=336&sz=14&tbnid=RHKePzUzxuEonM:&tbnh=127&tbnw=95&prev=/images%3Fq%3Dchris%2Beasley&hl=en&usg=__s-B6oN-fhpvZjOVG0WRiB7ancUk=&ei=tapXS5rzD4X80wSd4fz8BA&sa=X&oi=image_result&resnum=4&ct=image&ved=0CBAQ9QEwAw
"""
#stringnum = ''.join(map(str, map(ord, 'faux-mathematical')))
#print(stringnum)

docuNames = ["documents/Wiki" + str(i) + ".txt" for i in range(1, 13)]
numDocs = [0 for _ in range(12)]
numWords = [0 for _ in range(12)]
sizeDictionary = [set() for _ in range(12)]
sizeAlphabet = [set() for _ in range(12)]

# number of documents
# size of dictionary
# sum of words
# input string
# hashtable prime??

for i, e in enumerate(docuNames):
    file = open(e, encoding="utf-8")
    lines = file.readlines()
    file.close()
    for line in lines:
        for word in line.split(" "):
            if len(word) == 0:
                continue
            if word[-1] == "\n":
                word = word[:-1]
            if word == "---END.OF.DOCUMENT---":
                numDocs[i] = 1 + numDocs[i]
            numWords[i] += 1
            sizeDictionary[i].add(word)
            for char in word:
                sizeAlphabet[i].add(char)
file = open("dataInfo.txt", "w")
file.write("Names:\n")
for e in docuNames:
    file.write(str(e)+"|")
file.write("\nNumber of documents:\n")
for e in numDocs:
    file.write(str(e)+"|")
file.write("\nNumber of words:\n")
for e in numWords:
    file.write(str(e)+"|")
file.write("\nSize of dictionaries:\n")
for e in sizeDictionary:
    file.write(str(len(e))+"|")
file.write("\nSize of alphabet:\n")
for e in sizeAlphabet:
    file.write(str(len(e))+"|")