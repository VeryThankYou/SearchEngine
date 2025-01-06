import numpy as np
import matplotlib.pyplot as plt

# Longest word: link to some image:
"""
http://www.google.co.uk/imgres?imgurl=http://www.lesmills.co.nz/pt_photos/Chris%2520E1.jpg&imgrefurl=http://www.lesmills.co.nz/lesmills_custom.cfm%3F%26do%3Daction%26action%3Dchoose%2520PT%26clubid%3D1%2520OR%2520clubid%2520%3D%25202%2520OR%2520clubid%3D%25203%2520OR%2520clubid%2520%3D%25204%2520OR%2520clubid%2520%3D%25205%2520OR%2520clubid%2520%3D%25206%2520OR%2520clubid%2520%3D%25207%2520OR%2520clubid%2520%3D%25208%2520OR%2520clubid%2520%3D%25209%2520OR%2520clubid%2520%3D%252010&h=448&w=336&sz=14&tbnid=RHKePzUzxuEonM:&tbnh=127&tbnw=95&prev=/images%3Fq%3Dchris%2Beasley&hl=en&usg=__s-B6oN-fhpvZjOVG0WRiB7ancUk=&ei=tapXS5rzD4X80wSd4fz8BA&sa=X&oi=image_result&resnum=4&ct=image&ved=0CBAQ9QEwAw
"""
#stringnum = ''.join(map(str, map(ord, 'faux-mathematical')))
#print(stringnum)

file = open("HashSize11.txt", encoding="utf-8")
lines = file.readlines()
file.close()
uniqueWords = int(lines[0])
hashDistribution = [int(e) for e in lines[1].split("|")]
indexes = [i for i in range(len(hashDistribution))]
avg = [uniqueWords/len(hashDistribution) for _ in range(len(hashDistribution))]
plt.plot(indexes, hashDistribution, '.', label="Hash distribution")
plt.plot(indexes, avg, '--', label="Uniform distribution")
plt.xlabel("Hashtable index")
plt.ylabel("Words at index")
plt.legend()
plt.show()


cumDist = []
cumavg = []
for i in range(len(hashDistribution)):
    if i == 0:
        cumDist.append(hashDistribution[i])
        cumavg.append(avg[i])
    else:
        cumDist.append(hashDistribution[i] + cumDist[i-1])
        cumavg.append(avg[i] + cumavg[i-1])
plt.plot(indexes, cumDist, label="Cumulative hash distribution")
plt.plot(indexes, cumavg, '--', label="Cumulative uniform distribution")
plt.xlabel("Hashtable index")
plt.ylabel("Words at or below index")
plt.legend()
plt.show()