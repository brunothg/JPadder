package input.conrtoller.gui;

import java.awt.Component;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bno.swing2.dialog.ExceptionDialog;
import input.conrtoller.Constants;
import input.conrtoller.data.Padder;
import input.conrtoller.data.ScriptPadder;
import input.conrtoller.doa.ScriptPadderDao;

public class IO {

	private static final Logger LOG = LoggerFactory.getLogger(IO.class);

	private static Path last = Paths.get(".");

	public static void save(Padder padder, Component parent) {

		Path path = getPath(true, parent);
		if (path == null) {
			return;
		}

		try {
			if (padder instanceof ScriptPadder) {
				new ScriptPadderDao(path).store((ScriptPadder) padder);
			} else {
				throw new Exception(String.format("Can not save this type of padder %s", padder.getClass().getName()));
			}
		} catch (Exception e) {
			LOG.warn("Save error", e);
			ExceptionDialog.showExceptionDialog(parent, e);
		}
	}

	public static Padder load(Path p, Component parent) {

		Path path = getPath(false, parent);
		if (path == null) {
			return null;
		}

		try {
			return new ScriptPadderDao(path).load();
		} catch (Exception e) {
			LOG.warn("Load error", e);
			ExceptionDialog.showExceptionDialog(parent, e);
		}

		return null;
	}

	private static Path getPath(boolean save, Component parent) {
		JFileChooser fc = new JFileChooser(last.toFile());
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "Padder(*" + Constants.FILE_EXTENSION + ")";
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				return f.getName().endsWith(Constants.FILE_EXTENSION);
			}
		});

		int answer = (save) ? fc.showSaveDialog(parent) : fc.showOpenDialog(parent);
		if (answer == JFileChooser.APPROVE_OPTION) {
			last = Paths.get(fc.getSelectedFile().toURI());

			if (save && !last.getFileName().toString().endsWith(Constants.FILE_EXTENSION)) {
				last = last.getParent().resolve(last.getFileName().toString() + Constants.FILE_EXTENSION);
			}

			if (save && Files.exists(last)) {
				int choise = JOptionPane.showConfirmDialog(parent, "Do you want to overwrite this file?",
						"Already exists", JOptionPane.YES_NO_OPTION);
				if (choise == JOptionPane.NO_OPTION) {
					return null;
				}
			}

			return last;
		}

		return null;
	}
}
