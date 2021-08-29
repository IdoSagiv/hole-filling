# Hole Filling Library
An image processing library that fills holes in images.

## Technologies And Tools
This program is written in Java and using the OpenCV library.<br/>

## Overview
### The programs structure
The program consists of two main modules:
* Hole Filling library
* Command Line utility

The input for the program is:
* Path the original and hole-mask images
* z and epsilon values for the weight function
* Connectivity type (4 or 8 connected)

### The program work flow
1. Convert the image to a grayscale image
2. Normalize the pixels values to [0-1]
3. Apply the mask by assigning -1 values to hole pixels
4. Find the hole and boundary (for the specified connectivity type)
5. Compute the new value of each hole pixel using the specified weight function
6. Save the filled image

**Note** - The library itself gets as input a grayscale corrupted image with pixels values in the range of [0-1] and holes represented with -1 value.

## Usage
{image_path} {hole_image_path} {z} {epsilon} {connectivity_type}

## Example
Original image
<p align="center">
    <img src="images/Lenna.png" width="400" height="400">
    <img src="images/Lenna_gray.png" width="400" height="400">
</p>

Corrupted and Fixed images
<p align="center">
    <img src="images/mask3/Lenna_corrupted.png" width="400" height="400">
    <img src="images/mask3/Lenna_fixed.png" width="400" height="400">
</p>

* more examples could be found [here](https://github.com/IdoSagiv/hole-filling/tree/main/images)
