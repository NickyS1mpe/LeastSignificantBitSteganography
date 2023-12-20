import matplotlib.pyplot as plt
import cv2
import sys

# 参数读取
args = sys.argv

if len(args) < 2:
    print("Usage: python my_program.py <parameter>")
    sys.exit(1)

# 赋值
path = args[1]
name = args[2]

# 生成直方图
image = cv2.imread(path)
image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

r_values = image[:, :, 0].flatten()
g_values = image[:, :, 1].flatten()
b_values = image[:, :, 2].flatten()

plt.hist(r_values, bins=256, color='red', alpha=0.5, label='Red')
plt.hist(g_values, bins=256, color='green', alpha=0.5, label='Green')
plt.hist(b_values, bins=256, color='blue', alpha=0.5, label='Blue')

# 设置图像纵轴高度
plt.ylim([0, 6000])

plt.xlabel('Pixel Value')
plt.ylabel('Count')
plt.title('Histogram of RGB Image')
plt.legend()

# 保存结果
plt.savefig('./Res/' + name + '.png')

# 展示直方图
# plt.show()
