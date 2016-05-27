package input.controller.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import input.controller.logic.padder.Padder;
import input.controller.logic.padder.ScriptPadder;

public class Editor {
	private static final Logger LOG = LoggerFactory.getLogger(Editor.class);

	public static void edit(Padder padder, Component parent) {
		JPanel panel = getEditorPane(padder);
		if (panel == null) {
			LOG.warn("No editor found for '{}'", padder.getClass().getName());
			return;
		}

		JDialog dia = new JDialog(SwingUtilities.windowForComponent(parent));
		dia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dia.setLayout(new BorderLayout());
		dia.add(panel);
		dia.pack();
		dia.setLocationRelativeTo(parent);
		dia.setVisible(true);
	}

	private static JPanel getEditorPane(Padder padder) {

		if (padder instanceof ScriptPadder) {
			return new ScriptEditor((ScriptPadder) padder);
		}

		return null;
	}
}
