import numpy as np
import matplotlib.pyplot as plt

fileIds = [i for i in range(12)]
Ns = [4, 58, 126, 359, 842, 1745, 4182, 8246, 18004, 47026, 130429, 3035070]
ns = [4637, 31777, 51780, 101433, 166388, 271950, 496567, 794336, 1268782, 2087080, 3362624, 15093753]
Ts = [13884, 164932, 329408, 828401, 1672818, 3350449, 8360447, 16725137, 33460066, 67309922, 135880092, 993283720]

i1bs = [0.065681, 0.1520728, 0.21227539, 0.47014597, 1.0888062, 2.474582, 5.615693, 11.00213, 23.168879]
i3bs = [1.135883, 53.506283, 256.35843, 1198.2571, 2874.3618, 6390.7246, 23365.23, 65997.03]
i4bs = [0.0828328, 0.5639853, 1.6083951, 5.945152, 12.77735, 39.22215, 142.03801, 541.78296, 1628.5765, 6391.682, 16213.861]

plt.plot([Ts[i]*np.log(Ts[i]) for i in range(11)], i4bs)
plt.title("Linked list datastructure build-time")
plt.xlabel("T*n in each dataset")
plt.ylabel("Buildtime in seconds")
plt.show()