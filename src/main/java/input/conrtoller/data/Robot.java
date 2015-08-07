package input.conrtoller.data;

import java.awt.AWTException;
import java.awt.GraphicsDevice;

public class Robot extends java.awt.Robot {

	public Robot() throws AWTException {
		super();
	}

	public Robot(GraphicsDevice screen) throws AWTException {
		super(screen);
	}

}
