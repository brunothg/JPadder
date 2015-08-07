package input.conrtoller.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class SlidingControl extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel pnlName;
	private JProgressBar pbPositive;

	public SlidingControl(float value, String name) {
		createGui();
		setValue(value);
		setControlName(name);
	}

	public void setControlName(String name) {
		pnlName.setText(name);
	}

	public void setValue(float value) {

		if (value > 0) {
			pbPositive.setForeground(new Color(50, 205, 50));
		} else {
			pbPositive.setForeground(new Color(220, 20, 60));
		}
		pbPositive.setValue((int) (100 * Math.abs(value)));
		pbPositive.setString(((Float) value).toString());
	}

	private void createGui() {
		setBorder(new EmptyBorder(3, 3, 3, 3));
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		pnlName = new JLabel("Name");
		pnlName.setBorder(new EmptyBorder(0, 3, 0, 10));
		panel.add(pnlName, BorderLayout.WEST);

		pbPositive = new JProgressBar();
		add(pbPositive, BorderLayout.WEST);
		pbPositive.setPreferredSize(new Dimension(70, 30));
		pbPositive.setMinimum(0);
		pbPositive.setMaximum(+100);
		pbPositive.setBorder(new EmptyBorder(5, 5, 5, 5));
		pbPositive.setStringPainted(true);
	}

}
