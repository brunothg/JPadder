// console.debug(); console.info(); console.warn(); console.error();

// This function will be invoked at event time
function controllerEvent(event){
}

/*
ControllerEvent event

Controller getSource()
boolean isButtonEvent()
boolean isAxisEvent()
boolean isxAxisEvent()
boolean isyAxisEvent()
boolean isPovEvent()
boolean isxPovEvent()
boolean isyPovEvent()
boolean isPressed()
int getControlIndex()
*/

/*
Robot robot - Extract from -> http://docs.oracle.com/javase/7/docs/api/java/awt/Robot.html

keyPress(int keyCode)
keyRelease(int keyCode)
mouseMove(int x, int y)
mousePress(int buttons)
mouseRelease(int buttons)
mouseWheel(int wheelAmt)
delay(int ms)
*/

/*
Controller - Extract from -> http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Controller.html

float 	getAxisValue(int index)
Retrieve the value thats currently available on a specified axis.

float 	getPovX()
Get the X-Axis value of the POV on this controller

float 	getPovY()
Get the Y-Axis value of the POV on this controller

float 	getRXAxisValue()
Get the value from the RX axis if there is one.

float 	getRYAxisValue()
Get the value from the RY axis if there is one.

float 	getRZAxisValue()
Get the value from the RZ axis if there is one.

float 	getXAxisValue()
Get the value from the X axis if there is one.

float 	getYAxisValue()
Get the value from the Y axis if there is one.

float 	getZAxisValue()
Get the value from the Z axis if there is one.

boolean 	isButtonPressed(int index)
Check if a button is currently pressed
*/