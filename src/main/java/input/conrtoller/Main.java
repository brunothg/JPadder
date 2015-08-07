package input.conrtoller;

import javax.swing.UIManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;

import bno.swing2.dialog.ExceptionDialog;
import input.conrtoller.data.ControllerEventQueue;
import input.conrtoller.gui.GUI;

public class Main {

	public static void main(String[] args) {

		setLaF();

		try {
			_main(args);
		} catch (Exception e) {
			ExceptionDialog.showExceptionDialog(null, e);
			System.exit(-1);
		}

	}

	private static void _main(String[] args) throws Exception {
		iniInputDevices();

		GUI gui = new GUI();
		gui.setVisible(true);
	}

	private static void iniInputDevices() throws LWJGLException {

		if (!Controllers.isCreated()) {
			Controllers.create();
		}

		ControllerEventQueue.create();
	}

	private static void setLaF() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
	}

}
