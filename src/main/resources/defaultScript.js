// console.debug(); console.info(); console.warn(); console.error();

// This function will be invoked at event time
function controllerEvent(event){
	var source = event.getSource();
	
	if(event.isButtonEvent()){
		switch(event.getControlIndex()){
			case 0:
			// J -> 74
				if(event.isPressed()){
					robot.keyPress(74);
				}else{
					robot.keyRelease(74);
				}
			break;
			case 1:
			// K -> 75
				if(event.isPressed()){
					robot.keyPress(75);
				}else{
					robot.keyRelease(75);
				}
			break;
			case 2:
			// L -> 76
				if(event.isPressed()){
					robot.keyPress(76);
				}else{
					robot.keyRelease(76);
				}
			break;
			case 3:
			// I -> 73
				if(event.isPressed()){
					robot.keyPress(73);
				}else{
					robot.keyRelease(73);
				}
			break;
			case 4:
			// 1 -> 49
				if(event.isPressed()){
					robot.keyPress(49);
				}else{
					robot.keyRelease(49);
				}
			break;
			case 5:
			// 2 -> 50
				if(event.isPressed()){
					robot.keyPress(50);
				}else{
					robot.keyRelease(50);
				}
			break;
			case 6:
			// 3 -> 51
				if(event.isPressed()){
					robot.keyPress(51);
				}else{
					robot.keyRelease(51);
				}
			break;
			case 7:
			// 4 -> 52
				if(event.isPressed()){
					robot.keyPress(52);
				}else{
					robot.keyRelease(52);
				}
			break;
			case 8:
			// 5 -> 53
				if(event.isPressed()){
					robot.keyPress(53);
				}else{
					robot.keyRelease(53);
				}
			break;
			case 9:
			// 6 -> 54
				if(event.isPressed()){
					robot.keyPress(54);
				}else{
					robot.keyRelease(54);
				}
			break;
			case 10:
			// 7 -> 55
				if(event.isPressed()){
					robot.keyPress(55);
				}else{
					robot.keyRelease(55);
				}
			break;
			case 11:
			// 8 -> 56
				if(event.isPressed()){
					robot.keyPress(56);
				}else{
					robot.keyRelease(56);
				}
			break;
		}
	}else if(event.isPovEvent()){
		if(source.getPovX()>0.8){
			// D -> 68
			robot.keyPress(68);
		}else if(source.getPovX()<-0.8){
			// A -> 65
			robot.keyPress(65);
		}else{
			// Release
			robot.keyRelease(68);
			robot.keyRelease(65);
		}
		
		if(source.getPovY()>0.8){
			// S -> 83
			robot.keyPress(83);
		}else if(source.getPovY()<-0.8){
			// W -> 87
			robot.keyPress(87);
		}else{
			// Release
			robot.keyRelease(83);
			robot.keyRelease(87);
		}
	}else if(event.isAxisEvent() || event.isxAxisEvent() || event.isyAxisEvent()){
		// Need this complicated way, because some controllers(with multiple sticks)
		// will tell axisEvent for one stick and xAxis/yAxis for the other one
		switch(event.getControlIndex()){
			case 0:
			// Right-Vertical
				robot.mouseMoveRel(0, source.getAxisValue(0)*10);
			break;
			case 1:
			// Right-Horizontal
				robot.mouseMoveRel(source.getAxisValue(1)*10, 0);
			break;
			case 2:
			// Left-Vertical
				if(source.getAxisValue(2)>0.7){
					// S -> 83
					robot.keyPress(83);
				}else if(source.getAxisValue(2)<-0.7){
					// W -> 87
					robot.keyPress(87);
				}else{
					// Release
					robot.keyRelease(83);
					robot.keyRelease(87);
				}
			break;
			case 3:
			// Left-Horizontal
				if(source.getAxisValue(3)>0.7){
					// D -> 68
					robot.keyPress(68);
				}else if(source.getAxisValue(3)<-0.7){
					// A -> 65
					robot.keyPress(65);
				}else{
					// Release
					robot.keyRelease(68);
					robot.keyRelease(65);
				}
			break;
		}
	}
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
mouseMoveRel(int x, int y)
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