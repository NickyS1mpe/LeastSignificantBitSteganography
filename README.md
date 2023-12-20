Functions:
Text Message Steganography (encryptMes)

Encrypts text messages into images using various steganographic methods.
Parameters:
key: Encryption key (16 characters).
path1: Path to the original image.
path2: Path to the encrypted image.
path3: Path to the text file.
method: Steganographic method (LSB, MLSB, Diff).
random: Whether to use a pseudo-random sequence.
Text Message Extraction (decryptMes)

Extracts text messages from images using various steganographic methods.
Parameters:
key: Decryption key (16 characters).
path1: Path to the image with hidden text.
path2: Path to the decrypted text file.
method: Steganographic method (LSB, MLSB, Diff).
random: Whether to use a pseudo-random sequence.
Binary Image Steganography (encryptPic)

Encrypts binary images into other images using various steganographic methods.
Parameters:
key: Encryption key (16 characters).
path1: Path to the original binary image.
path2: Path to the encrypted image.
path3: Path to the binary image file.
method: Steganographic method (insert, LSB, MLSB, Diff).
random: Whether to use a pseudo-random sequence.
Binary Image Extraction (decryptPic)

Extracts binary images from other images using various steganographic methods.
Parameters:
key: Decryption key (16 characters).
path1: Path to the image with hidden binary data.
path2: Path to the extracted binary image file.
method: Steganographic method (insert, LSB, MLSB, Diff).
random: Whether to use a pseudo-random sequence.
Binary Image Transformation (transBin)

Converts grayscale images to binary images using a specified threshold.
Parameters:
threshold: Threshold value for conversion.
path1: Path to the original image.
path2: Path to the transformed binary image.
Performance Evaluation (evaluate)

Evaluates the Mean Squared Error (MSE) and Peak Signal-to-Noise Ratio (PSNR) between two images.
Parameters:
path1: Path to the first image.
path2: Path to the second image.
Image Comparison (compare)

Compares two images and generates a binary image highlighting differences.
Parameters:
path1: Path to the first image.
path2: Path to the second image.
write: Path to the output binary comparison image.
RS Steganalysis (RS_Analysis)

Performs steganalysis on images using the RS (Reed-Solomon) method.
Parameters:
n: Group size.
ml0: Initial deviation value.
path: Path to the image for analysis.
Edge Analysis (ea_Analysis)

Analyzes edges in images using Sobel edge detection.
Parameters:
threshold1: Upper threshold for edge detection.
threshold2: Lower threshold for edge detection.
path1: Path to the original image.
path2: Path to the analyzed edge image.
Edge Adaptive Text Encryption (ea_Encrypt)

Encrypts text messages into images based on edge adaptive analysis.
Parameters:
threshold1: Upper threshold for edge detection.
threshold2: Lower threshold for edge detection.
deviation: Random sequence deviation.
key: Encryption key (16 characters).
path1: Path to the original image.
path2: Path to the encrypted image.
path3: Path to the text file.
Edge Adaptive Text Decryption (ea_Decrypt)

Decrypts text messages from images based on edge adaptive analysis.
Parameters:
key: Decryption key (16 characters).
path1: Path to the image with hidden text.
path2: Path to the decrypted text file.
Edge Adaptive Binary Image Encryption (ea_EncryptPic)

Encrypts binary images into images based on edge adaptive analysis.
Parameters:
threshold1: Upper threshold for edge detection.
threshold2: Lower threshold for edge detection.
deviation: Random sequence deviation.
key: Encryption key (16 characters).
path1: Path to the original binary image.
path2: Path to the encrypted image.
path3: Path to the binary image file.
Edge Adaptive Binary Image Decryption (ea_DecryptPic)

Decrypts binary images from images based on edge adaptive analysis.
Parameters:
key: Decryption key (16 characters).
path1: Path to the image with hidden binary data.
path2: Path to the extracted binary image file.
Get Grayscale Image (getGrayImage)

Performs various operations on grayscale images (e.g., getting grayscale, specified layer, removing layer).
Parameters:
layer: Layer to be processed.
type: Processing type (0: grayscale, 1: specified layer, 2: remove specified layer).
path1: Path to the original image.
path2: Path to the processed image.
Add Noise to Image (addNoise)

Adds salt-and-pepper noise to images.
Parameters:
probability: Probability of noise generation.
read: Path to the original image.
write: Path to the image with added noise.
Get Current Time (getTime)

Returns the current time in the format "yyyy-MM-dd HH:mm:ss".
Get Image Histogram (getHistogram)

Generates a histogram for an image and saves it to a file.
Parameters:
path: Path to the image.
Usage:
Make sure to have Java installed on your system.
Compile the code.
Run the mainService class with appropriate function calls based on your requirements.
