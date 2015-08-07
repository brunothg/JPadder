package input.conrtoller.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.engine.image.InternalImage;
import input.conrtoller.Constants;

public class OnOffControl extends JPanel implements ActionListener {

	private static final Logger LOG = LoggerFactory.getLogger(OnOffControl.class);
	private static final long serialVersionUID = 1L;
	private static final String ACTION_ACTION = "action";

	private JTextField tfAction;
	private JLabel lblIndicator;
	private JLabel lblName;

	public OnOffControl(boolean on, String name) {
		createGui();
		setIndicator(on);
		setControlName(name);
	}

	private void createGui() {
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(3, 3, 3, 3));

		lblIndicator = new JLabel("");
		lblIndicator.setBorder(new EmptyBorder(5, 5, 5, 5));
		lblIndicator.setIcon(new ImageIcon(InternalImage.loadFromPath(Constants.imageFolder, "off.png")));
		add(lblIndicator, BorderLayout.WEST);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		lblName = new JLabel("Name");
		lblName.setBorder(new EmptyBorder(0, 0, 0, 10));
		panel.add(lblName, BorderLayout.WEST);

		tfAction = new JTextField();
		panel.add(tfAction, BorderLayout.CENTER);
		tfAction.setColumns(10);

		JButton btnAction = new JButton("Edit");
		btnAction.setActionCommand(ACTION_ACTION);
		btnAction.addActionListener(this);
		panel.add(btnAction, BorderLayout.EAST);

	}

	public void setControlName(String name) {
		lblName.setText(name);
	}

	public void setIndicator(boolean on) {
		if (on) {
			lblIndicator.setIcon(new ImageIcon(InternalImage.loadFromPath(Constants.imageFolder, "on.png")));
		} else {
			lblIndicator.setIcon(new ImageIcon(InternalImage.loadFromPath(Constants.imageFolder, "off.png")));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		default:
			LOG.warn("Unknown action: '{}'", e.getActionCommand());
			break;
		}
	}

}
