In Edge Adaptive LSB steganography, the least significant bits of the pixels are modified based on the edges in the image. This is done to make the hidden message less noticeable in the smooth areas of the image, while being more resistant to detection in the edge areas of the image.

Here are the steps for using Edge Adaptive LSB steganography:

Convert the secret message into binary format.
Determine the edges in the image using an edge detection algorithm like Canny edge detector or Sobel edge detector.
Divide the image into edge and non-edge regions based on the detected edges.
In the non-edge regions, modify the least significant bits of the pixels to hide the secret message, using a fixed embedding rate.
In the edge regions, modify the least significant bits of the pixels based on the edge strength. Higher edge strength pixels will have more bits modified, while lower edge strength pixels will have fewer bits modified.
To decode the message, use the same edge detection algorithm to determine the edge and non-edge regions, and extract the least significant bits from the pixels in the non-edge regions.
This method can improve the security of the hidden message, as it makes it less noticeable in the smooth areas of the image, while being more resistant to detection in the edge areas of the image.

to smooth: insert 1 bit
to mid: insert 2bits
to sharp: insert 4 bits

文字加密：
    先头部填充一个字节的16进制消息长度，再进行base64加密（使可以加密中文字符），再进行aes加密

图片加密方案：
    1、基础的LSB
    2、MLSB:LSB值随机+-1
    3、差分:相邻LSB间差值存储信息，LSB值随机+-1

评估加密质量:
    MSE&PSNR

网页服务步骤：
    登录->获得用户id->获得id下存储的图片和文本->选择要加密的图片->选择要加密的文本/输入要加密的文字->输入/随机获得加密key->进行加密->加密结果存储于服务器中->可下载/预览/图像比对/获得图像信息