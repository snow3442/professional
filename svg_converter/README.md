# Java SVG Converter

Java SVG Converter Allows a user to choose any image and convert it 
to Scalable Vector Graphics (SVG) and save it to desktop

## Getting Started
This project is built with Intellij.
Clone the repository and open the project with Intellij
(Other IDEs might require a few additional configuration steps)

### Prerequisites

JDK 11, Javafx 9+, any Java IDE, Gradle 5

### Project Configuration notes: 

This project uses JDK 11 to make it work with Gradle 5.
As time goes on, please make sure your gradle version works with your JDK version,
as both are constantly upgrading. For more details, please see:
https://www.jetbrains.com/help/idea/gradle-jvm-selection.html

## Functionalities
*User can choose any image file and load it to the software
*User can choose cluster the image by colors and obtain a list 
 of the colors that exist in the image
*User can choose the any set of colors and re-render the graphics,
 in other words, the user can drop any set of colors
*User can convert the desired re-rendered graphics to SVG and save it 
 as an SVG file
## Deployment
Deploy as jar or a batch file. 
## Built With

* [Gradle](https://gradle.org/) - Dependency Framework
* [JDK 11](https://www.oracle.com/java/technologies/javase-downloads.html) - 
* [JavaFX](https://gluonhq.com/products/javafx/) - Graphical User Interface Framework

## How to Use:
When Application starts, the user can see a window with 1 file menu "File"
1) Click on "choose file" and select your image
2) Once image is loaded onto the UI, click on "File" again, choose "Convert"
3) the Color Selection Window will pop up, estimate the number of colors that
   the your image contains, enter a number slightly higher than your estimation
   and click on cluster
4) A list of colors will be shown in the scrollpane below, select on the checkboxes
   beside the colors that you do NOT want to include in your svg conversion process, 
   (meaning you don't want those colors in your final svg output), and press "Preview".
   (Note: you must preview first before pressing "Convert To Svg", otherwise, your selection
   won't be taken into account). 
5) Once you are happy with the preview, press "Convert to Svg" button and then a 
   drawing panel will pop up, examine the drawing/graphics of each color, tune the 
   parameters: "Suppress Speckles", "Smooth Corners", "Optimize Paths" until you are
   satisfied with the results
6) click on "file" -> "save as svg" and select your location and file name and click 
   on ok (note you should not add .svg extension, just write your file name, eg: output,
   your file will be saved as output.svg)
   
## Author

* **Jianqiu Chen**

## License
This project is free of use by anyone

## Improvement Ideas
1. Better styled UI
2. Utilities to do the conversions for multiple files at once
3. auto detect number of clusters for the user
