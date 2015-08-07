package input.conrtoller.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bno.swing2.dialog.ExceptionDialog;
import input.conrtoller.data.ScriptPadder;
import java.awt.Dimension;

public class ScriptEditor extends JPanel implements ActionListener {

	private static final Logger LOG = LoggerFactory.getLogger(ScriptEditor.class);
	private static final long serialVersionUID = 1L;
	private static final String ACTION_OK = "ok";
	private static final String ACTION_CANCEL = "cancel";

	private ScriptPadder padder;

	private JTextArea textArea;
	private JLabel lblKeyCode;
	private JLabel lblButtonCode;

	public ScriptEditor(ScriptPadder padder) {
		setBorder(new EmptyBorder(3, 3, 3, 3));
		this.padder = padder;

		createGui();
		addCodeListener();
	}

	private void createGui() {
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(panel, BorderLayout.SOUTH);

		JButton btnOk = new JButton("Ok");
		btnOk.setActionCommand(ACTION_OK);
		btnOk.addActionListener(this);
		panel.add(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand(ACTION_CANCEL);
		btnCancel.addActionListener(this);
		panel.add(btnCancel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(200, 200));
		add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		textArea.setText(padder.getScript());
		scrollPane.setViewportView(textArea);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(panel_1, BorderLayout.NORTH);

		JLabel lblNewLabel_1 = new JLabel("KeyCode: ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_1.add(lblNewLabel_1);

		lblKeyCode = new JLabel("<N/A>");
		panel_1.add(lblKeyCode);

		JLabel lblNewLabel = new JLabel("ButtonCode: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_1.add(lblNewLabel);

		lblButtonCode = new JLabel("<N/A>");
		panel_1.add(lblButtonCode);

	}

	private void addCodeListener() {
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblKeyCode.setText("" + e.getKeyCode());
			}
		});
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				lblButtonCode.setText("" + e.getButton());
			}
		});
	}

	private void fireCancelEvent() {
		SwingUtilities.windowForComponent(this).dispose();
	}

	private void fireOkEvent() {
		try {
			padder.setScript(textArea.getText());
		} catch (ScriptException e) {
			LOG.warn("False custom script", e);
			ExceptionDialog.showExceptionDialog(this, "Error in script", e.getMessage(), e);
		}
		SwingUtilities.windowForComponent(this).dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case ACTION_CANCEL:
			fireCancelEvent();
			break;
		case ACTION_OK:
			fireOkEvent();
			break;
		default:
			LOG.warn("Unknown action: '{}'", e.getActionCommand());
			break;
		}
	}

}
