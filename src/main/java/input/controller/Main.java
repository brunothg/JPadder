package input.controller;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bno.swing2.dialog.ExceptionDialog;
import input.controller.gui.GUI;
import input.controller.logic.Robot;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		setLaF();

		try {
			_main(args);
		} catch (Exception e) {
			LOG.error("Unknown error", e);
			ExceptionDialog.showExceptionDialog(null, e);
			System.exit(-1);
		}

	}

	private static void _main(String[] args) throws Exception {
		Robot.iniInputDevices();

		GUI gui = new GUI();
		gui.setVisible(true);
	}

	private static void setLaF() {
		LOG.debug("Settings LaF");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOG.warn("Setting LaF failed", e);
		}
	}

}
