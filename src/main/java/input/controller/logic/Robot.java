package input.controller.logic;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.MouseInfo;
import java.awt.Point;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author marvin
 *
 *         Extended Robot class.
 */
public class Robot extends java.awt.Robot {
	private static final Logger LOG = LoggerFactory.getLogger(Robot.class);

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

	public static void iniInputDevices() throws LWJGLException {
		LOG.debug("Initialize input devices");
		if (Controllers.isCreated()) {
			Controllers.destroy();
		}
		Controllers.create();

		LOG.debug("Create event queue");
		ControllerEventQueue.create();
	}
}
