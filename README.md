# Image Sensor Transformation

## Prerequisites
You will have to have the following application installed:

* Android Studio and the corresponding SDK
* The cell phone should at least have the accelerometer, proximity senor and magnetic sensor.

## Overview
* With the three senors above, users are able to make modifications on image selected using different actions.
* Users have the option to save the image they like and load the image they want to apply filters on.
* When shaking the device, a random filter will be applied to the current source image instead of the original image imported.

## Project Documentation
* Code annotation is available in line.
* Documentation are generated using Javadoc (Details can be found in folder "Documentation" under the root directory)

## Project Management and Testing
* Use Git to manage this repository
* The app runs on Android API Level 26 (set min SDK to 23, target can still be 26)
* Require testing with a physical phone
* Work distribution:
  * Yao Shi - Working on Application Interface designs and Sensor Listener Connection
  * Shuyu Chen - Designing Image filters and Implement Listeners
  * Bhargav Sonani - Connecting Sensor Listeners with Image filters
  * Nakul Patel - Connecting Sensor Listeners with Image filters 

## Built with
* [Picasso-Transformations](https://github.com/wasabeef/picasso-transformations) - A Set of Image Filters
* [mukeshsolanki/photofilter](https://github.com/mukeshsolanki/photofilter) - Another Set of Image Filters
* [tbouron/ShakeDetector](https://github.com/tbouron/ShakeDetector) - Used to detect shaking

## Credit to
* @Yao Shi
* @Shuyu Chen
* @Bhargav Sonani
* @Nakul Patel
