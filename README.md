# Least Significant Bit Steganography
## Overview

The Java LSB Image Steganography Toolkit is a collection of functions for text steganography, image processing, and analysis. This toolkit provides a set of functionalities to encrypt and decrypt messages, hide binary images within other images, and perform various image analyses.

## Usage

Make sure to have Java installed on your system. Compile the code and run the `mainService` class with appropriate function calls based on your requirements.

## Functions

### 1. Text Message Steganography (`encryptMes`)

Encrypts text messages into images using various steganographic methods.

#### Parameters:
- `key`: Encryption key (16 characters).
- `path1`: Path to the original image.
- `path2`: Path to the encrypted image.
- `path3`: Path to the text file.
- `method`: Steganographic method (LSB, MLSB, Diff).
- `random`: Whether to use a pseudo-random sequence.

**Usage:**
```java
mainService.encryptMes("key", "path/to/original/image", "path/to/encrypted/image", "path/to/text/file", "LSB", true);
```

### 2. Text Message Extraction (`decryptMes`)

Extracts text messages from images using various steganographic methods.

#### Parameters:
- `key`: Decryption key (16 characters).
- `path1`: Path to the image with hidden text.
- `path2`: Path to the decrypted text file.
- `method`: Steganographic method (LSB, MLSB, Diff).
- `random`: Whether to use a pseudo-random sequence.

**Usage:**
```java
mainService.decryptMes("key", "path/to/image/with/text", "path/to/decrypted/text/file", "LSB", true);
```

### 3. Binary Image Steganography (`encryptPic`)

Encrypts binary images into other images using various steganographic methods.

#### Parameters:
- `key`: Encryption key (16 characters).
- `path1`: Path to the original binary image.
- `path2`: Path to the encrypted image.
- `path3`: Path to the binary image file.
- `method`: Steganographic method (insert, LSB, MLSB, Diff).
- `random`: Whether to use a pseudo-random sequence.

**Usage:**
```java
mainService.encryptPic("key", "path/to/original/binary/image", "path/to/encrypted/image", "path/to/binary/image/file", "LSB", true);
```

### 4. Binary Image Extraction (`decryptPic`)

Extracts binary images from other images using various steganographic methods.

#### Parameters:
- `key`: Decryption key (16 characters).
- `path1`: Path to the image with hidden binary data.
- `path2`: Path to the extracted binary image file.
- `method`: Steganographic method (insert, LSB, MLSB, Diff).
- `random`: Whether to use a pseudo-random sequence.

**Usage:**
```java
mainService.decryptPic("key", "path/to/image/with/binary/data", "path/to/extracted/binary/image/file", "LSB", true);
```

### 5. Binary Image Transformation (`transBin`)

Converts grayscale images to binary images using a specified threshold.

#### Parameters:
- `threshold`: Threshold value for conversion.
- `path1`: Path to the original image.
- `path2`: Path to the transformed binary image.

**Usage:**
```java
mainService.transBin(128, "path/to/original/image", "path/to/transformed/binary/image");
```

### 6. Performance Evaluation (`evaluate`)

Evaluates the Mean Squared Error (MSE) and Peak Signal-to-Noise Ratio (PSNR) between two images.

#### Parameters:
- `path1`: Path to the first image.
- `path2`: Path to the second image.

**Usage:**
```java
mainService.evaluate("path/to/first/image", "path/to/second/image");
```

### 7. Image Comparison (`compare`)

Compares two images and generates a binary image highlighting differences.

#### Parameters:
- `path1`: Path to the first image.
- `path2`: Path to the second image.
- `write`: Path to the output binary comparison image.

**Usage:**
```java
mainService.compare("path/to/first/image", "path/to/second/image", "path/to/output/binary/image");
```

### 8. RS Steganalysis (`RS_Analysis`)

Performs steganalysis on images using the RS (Reed-Solomon) method.

#### Parameters:
- `n`: Group size.
- `ml0`: Initial deviation value.
- `path`: Path to the image for analysis.

**Usage:**
```java
mainService.RS_Analysis(5, 0.05, "path/to/image/for/analysis");
```

### 9. Edge Analysis (`ea_Analysis`)

Analyzes edges in images using Sobel edge detection.

#### Parameters:
- `threshold1`: Upper threshold for edge detection.
- `threshold2`: Lower threshold for edge detection.
- `path1`: Path to the original image.
- `path2`: Path to the analyzed edge image.

**Usage:**
```java
mainService.ea_Analysis(50, 30, "path/to/original/image", "path/to/analyzed/edge/image");
```

### 10. Edge Adaptive Text Encryption (`ea_Encrypt`)

Encrypts text messages into images based on edge adaptive analysis.

#### Parameters:
- `threshold1`: Upper threshold for edge detection.
- `threshold2`: Lower threshold for edge detection.
- `deviation`: Random sequence deviation.
- `key`: Encryption key (16 characters).
- `path1`: Path to the original image.
- `path2`: Path to the encrypted image.
- `path3`: Path to the text file.

**Usage:**
```java
mainService.ea_Encrypt(50, 30, 5, "key", "path/to/original/image", "path/to/encrypted/image", "path/to/text/file");
```

### 11. Edge Adaptive Text Decryption (`ea_Decrypt`)

Decrypts text messages from images based on edge adaptive analysis.

#### Parameters:
- `key`: Decryption key (16 characters).
- `path1`: Path to the image with hidden text.
- `path2`: Path to the decrypted text file.

**Usage:**
```java
mainService.ea_Decrypt("key", "path/to/image/with/text", "path/to/decrypted/text/file");
```

### 12. Edge Adaptive Binary Image Encryption (`ea_EncryptPic`)

Encrypts binary images into images based on edge adaptive analysis.

#### Parameters:
- `threshold1`: Upper threshold for edge detection.
- `threshold2`: Lower threshold for edge detection.
- `deviation`: Random sequence deviation.
- `key`: Encryption key (16 characters).
- `path1`: Path to the original binary image.
- `path2`: Path to the encrypted image.
- `path3`: Path to the binary image file.

**Usage:**
```java
mainService.ea_EncryptPic(upperThreshold, lowerThreshold, deviation, "key", "path/to/original/binary/image", "path/to/encrypted/image", "path/to/binary/image/file");
```

### 13. Edge Adaptive Binary Image Decryption (`ea_DecryptPic`)

Decrypts binary images from images based on edge adaptive analysis.

#### Parameters:
- `key`: Decryption key (16 characters).
- `path1`: Path to the image with hidden binary data.
- `path2`: Path to the extracted binary image file.

**Usage:**
```java
mainService.ea_DecryptPic("key", "path/to/image/with/binary/data", "path/to/extracted/binary/image/file");
```

### 14. Get Grayscale Image (`getGrayImage`)

Performs various operations on grayscale images (e.g., getting grayscale, specified layer, removing layer).

#### Parameters:
- `layer`: Layer to be processed.
- `type`: Processing type (0: grayscale, 1: specified layer, 2: remove specified layer).
- `path1`: Path to the original image.
- `path2`: Path to the processed image.

**Usage:**
```java
mainService.getGrayImage(layer, type, "path/to/original/image", "path/to/processed/image");
```

### 15. Add Noise to Image (`addNoise`)

Adds salt-and-pepper noise to images.

#### Parameters:
- `probability`: Probability of noise generation.
- `read`: Path to the original image.
- `write`: Path to the image with added noise.

**Usage:**
```java
mainService.addNoise(probability, "path/to/original/image", "path/to/image/with/noise");
```

### 16. Get Current Time (`getTime`)

Returns the current time in the format "yyyy-MM-dd HH:mm:ss".

**Usage:**
```java
String currentTime = mainService.getTime();
System.out.println(currentTime);
```

### 17. Get Image Histogram (`getHistogram`)

Generates a histogram for an image and saves it to a file.

#### Parameters:
- `path`: Path to the image.

**Usage:**
```java
mainService.getHistogram("path/to/image");
```


