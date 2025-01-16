import numpy as np
import matplotlib.pyplot as plt

fileIds = [i for i in range(12)]
Ns = [4, 58, 126, 359, 842, 1745, 4182, 8246, 18004, 47026, 130429, 3035070]
ns = [4637, 31777, 51780, 101433, 166388, 271950, 496567, 794336, 1268782, 2087080, 3362624, 15093753]
Ts = [13884, 164932, 329408, 828401, 1672818, 3350449, 8360447, 16725137, 33460066, 67309922, 135880092, 993283720]
Ss = [79180, 880909, 1757733, 4392786, 8787002, 17575609, 43957951, 87937173, 175886344, 351359337, 701286633, 5141117932]


i1bs = [0.065681, 0.1520728, 0.21227539, 0.47014597, 1.0888062, 2.474582, 5.615693, 11.00213, 23.168879]
i3bs = [1.135883, 53.506283, 256.35843, 1198.2571, 2874.3618, 6390.7246, 23365.23, 65997.03]
i4bs = [0.0828328, 0.5639853, 1.6083951, 5.945152, 12.77735, 39.22215, 142.03801, 541.78296, 1628.5765, 6391.682, 16213.861]
i5bs = [0.0671258, 0.7137383, 2.5871959, 7.624978, 27.615747, 61.485825, 245.6389, 848.88794, 2636.5195, 9983.88, 7607.1064]
i7bs = [0.064962, 0.5208277, 1.6232384, 8.012533, 24.271368, 65.40166, 310.01883, 1032.0461, 3330.799, 10641.775, 36744.023]

i1mem = [27759, 329747, 658583, 1656135, 3344003, 6697531, 16712745, 33434065, 66884511]
i3mem = [1.135883, 53.506283, 256.35843, 1198.2571, 2874.3618, 6390.7246, 23365.23, 65997.03]
i4mem = [0.0828328, 0.5639853, 1.6083951, 5.945152, 12.77735, 39.22215, 142.03801, 541.78296, 1628.5765, 6391.682, 16213.861]
i5mem = [0.0671258, 0.7137383, 2.5871959, 7.624978, 27.615747, 61.485825, 245.6389, 848.88794, 2636.5195, 9983.88, 7607.1064]
i7mem = [131541, 424660, 715662, 1567616, 2964854, 5656201, 13391480, 26081319, 51139658, 102516668, 207483646]

i1ssum = [27759, 329747, 658583, 1656135, 3344003, 6697531, 16712745, 33434065, 66884511]
i3ssum = [1.135883, 53.506283, 256.35843, 1198.2571, 2874.3618, 6390.7246, 23365.23, 65997.03]
i4ssum = [0.0828328, 0.5639853, 1.6083951, 5.945152, 12.77735, 39.22215, 142.03801, 541.78296, 1628.5765, 6391.682, 16213.861]
i5ssum = [0.0671258, 0.7137383, 2.5871959, 7.624978, 27.615747, 61.485825, 245.6389, 848.88794, 2636.5195, 9983.88, 7607.1064]
i7ssum = [131541, 424660, 715662, 1567616, 2964854, 5656201, 13391480, 26081319, 51139658, 102516668, 207483646]

for i in range(len(i1bs)):
    file = open("Testing/TimeTestingIndex1f" + str(i+1) + ".txt", encoding="utf-8")
    lines = file.readlines()
    file.close()
    i1mem[i] = int(lines[3])
    searchtime = 0
    for j in range(5, 15):
        searchtime += float(lines[j].split(":")[-1][:-1])
    i1ssum[i] = searchtime

for i in range(len(i3bs)):
    file = open("Testing/TimeTestingIndex3f" + str(i+1) + ".txt", encoding="utf-8")
    lines = file.readlines()
    file.close()
    i3mem[i] = int(lines[3])
    searchtime = 0
    for j in range(5, 15):
        searchtime += float(lines[j].split(":")[-1][:-1])
    i3ssum[i] = searchtime
    
for i in range(len(i4bs)):
    file = open("Testing/TimeTestingIndex4f" + str(i+1) + ".txt", encoding="utf-8")
    lines = file.readlines()
    file.close()
    i4mem[i] = int(lines[3])
    searchtime = 0
    for j in range(5, 15):
        searchtime += float(lines[j].split(":")[-1][:-1])
    i4ssum[i] = searchtime

for i in range(len(i5bs)):
    file = open("Testing/TimeTestingIndex5f" + str(i+1) + "x.txt", encoding="utf-8")
    lines = file.readlines()
    file.close()
    for j in range(21, 33):
        searchtime += float(lines[j].split(":")[-1][:-1])
    i5ssum[i] = searchtime

for i in range(len(i7bs)):
    file = open("Testing/TimeTestingIndex7f" + str(i+1) + ".txt", encoding="utf-8")
    lines = file.readlines()
    file.close()
    i7mem[i] = int(lines[3])

plt.plot([Ts[i] for i in range(9)], i1ssum, '.', color="blue")
plt.plot([Ts[i] for i in range(9)], i1ssum, color="blue")
plt.title("Search time for naive datastructure")
plt.xlabel("T in each dataset")
plt.ylabel("Time in seconds")
plt.show()

plt.plot([ns[i] for i in range(8)], i3ssum, '.', color="blue")
plt.plot([ns[i] for i in range(8)], i3ssum, color="blue")
plt.title("Search time for the linked list datastructure")
plt.xlabel("n in each dataset")
plt.ylabel("Time in seconds")
plt.show()

plt.plot([ns[i] for i in range(11)], i5ssum, '.', color="blue")
plt.plot([ns[i] for i in range(11)], i5ssum, color="blue")
plt.title("Search time for hash-table datastructure (and-or)")
plt.xlabel("n in each dataset")
plt.ylabel("Time in seconds")
plt.show()

plt.plot([Ss[i] for i in range(11)], i5bs, '.', color="blue")
plt.plot([Ss[i] for i in range(11)], i5bs, color="blue")
plt.title("Buildtime for hash-table datastructure (and-or)")
plt.xlabel("S in each dataset")
plt.ylabel("Time in seconds")
plt.show()

plt.plot([Ss[i] for i in range(11)], i7bs, '.', color="blue")
plt.plot([Ss[i] for i in range(11)], i7bs, color="blue")
plt.title("Buildtime for hash-table datastructure (ranked)")
plt.xlabel("S in each dataset")
plt.ylabel("Time in seconds")
plt.show()
"""
file = open("HashTime.txt", encoding="utf-8")
lines = file.readlines()
file.close()
line = lines[0][:-1]
times = [int(e) for e in line.split("|")]
lengths = [i for i in range(len(times))]
plt.plot(lengths, times)
plt.xlabel("Length of string")
plt.ylabel("Time to hash in nanoseconds")
plt.show()
"""