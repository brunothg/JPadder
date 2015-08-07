package input.conrtoller.data;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.MouseInfo;
import java.awt.Point;

public class Robot extends java.awt.Robot {

	public Robot() throws AWTException {
		super();
	}

	public Robot(GraphicsDevice screen) throws AWTException {
		super(screen);
	}

	public synchronized void mouseMoveRel(int x, int y) {
		Point location = MouseInfo.getPointerInfo().getLocation();
		mouseMove(location.x + x, location.y + y);
	}
}
