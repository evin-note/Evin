#!/usr/bin/env python
# coding: utf-8

# In[14]:


import cv2
import numpy as np
from matplotlib import pyplot as plt
img = cv2.imread("img/input/marimo.png")
gray = cv2.imread("img/input/marimo.png",0)
rgba =  cv2.imread("img/input/marimo.png",-1)

b, g, r = img[:,:,0], img[:,:,1], img[:,:,2]
#RGB
hist_r, bins = np.histogram(r.ravel(),256,[0,256])
hist_g, bins = np.histogram(g.ravel(),256,[0,256])
hist_b, bins = np.histogram(b.ravel(),256,[0,256])

#Gray
hist, bins = np.histogram(gray.ravel(),256,[0,256])


# In[12]:


cv2.imwrite("img/output/gmarimo.jpg",gray)


# In[17]:


"""
###ヒストグラム
-------------
このヒストグラムは
縦軸：画素数
横軸：画素数(階調値)
つまり、「画像中に画素値が〇〇の画素は何個あるのか」を示します。

形状 　	画像の状態
山が左寄り	暗い画像
山が右寄り	明るい画像
山が中央寄り	コントラストが低い

-------------
"""

plt.xlim(0, 255)
plt.plot(hist_r, "-r", label="Red")
plt.plot(hist_g, "-g", label="Green")
plt.plot(hist_b, "-b", label="Blue")
plt.xlabel("Pixel value", fontsize=20)
plt.ylabel("Number of pixels", fontsize=20)
plt.legend()
plt.grid()
plt.show()
plt.xlim(0, 255)
plt.plot(hist)
plt.xlabel("Pixel value", fontsize=20)
plt.ylabel("Number of pixels", fontsize=20)
plt.grid()
plt.show()


# In[21]:


"""
ガンマ補正
画像のコントラストを調節し、視認しやすくするのによく使われる

url<https://algorithm.joho.info/image-processing/gamma-correction/>
"""
gamma = 1.0

# 画素値の最大値
imax = gray.max() 
    
# ガンマ補正
gray = imax * (gray / imax)**(1/gamma)

# 結果の出力
cv2.imwrite("img/output/gamma3.jpg", gray)


# In[27]:


def equalize_hist(src):
   # 画像の高さ・幅を取得
   h, w = src.shape[0], src.shape[1]
   
   # 全画素数
   s = w * h
   
   # 画素値の最大値
   imax = src.max()
   
   # ヒストグラムの算出
   hist, bins = np.histogram(src.ravel(),256,[0,256])

   # 出力画像用の配列（要素は全て0）
   dst = np.empty((h,w))

   for y in range(0, h):
       for x in range(0, w):
           # ヒストグラム平均化の計算式
           dst[y][x] = np.sum(hist[0: src[y][x]]) * (imax / s)

   return dst



gray2 = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
# 方法2(OpenCVで実装)
dst2 = cv2.equalizeHist(gray2)
# 結果の出力
cv2.imwrite("img/output/Histmarimo.jpg", dst2)

