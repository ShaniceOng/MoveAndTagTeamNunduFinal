Visualisation folder contains the IntelliJ project for the Visualisation Tool. 
To make it run properly from IntelliJ you must add "core.jar" (from Processing folder) as a global library in IntelliJ.
Also you must enter the absolute directory paths to robots.txt and output.txt at the top of the Main.java source code as Strings
inputFile and routesFile.

VisualisationProcessing folder contains the Processing sketch that will run instantly in the Processing application.
Important info/keys for using visualisation/simulation:
-Input the level (1-30) you wish to simulate as the global variable "level" in the Processing sketch.
-Enter the absolute directory path to robots.txt as the String inputFile at the top of the sketch. (Remember use '/' and not '\')
-Enter the absolute directory path to output.txt as String routesFile at the top of the sketch.

How to use visualisation tool:
-Use key B to progress the simulation (hold down to visualise routes faster), use key V to rewind visualisation similarly.
-Use keys WASD to pan camera, note that you must press the key B to update the screen after you have pressed a key to pan the camera.
-Use key O to zoom out and P to zoom in, similarly, to update the drawing, press the key B after you have pressed a key to zoom in.

Enjoy!
