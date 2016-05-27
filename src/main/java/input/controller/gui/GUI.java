package input.controller.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bno.swing2.widget.tab.ApplicationTabPanel;
import input.controller.logic.ControllerEventQueue;
import input.controller.logic.ControllerEventQueue.ControllerListener;
import input.controller.logic.padder.Padder;
import input.controller.logic.padder.ScriptPadder;

public class GUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(GUI.class);

	private static final String ACTION_NEW_PROFILE = "new_profile";
	private static final String ACTION_LOAD_PROFILE = "load_profile";

	private ApplicationTabPanel profileTabPanel;

	public GUI() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/game/engine/media/icon.png")));
		setTitle("JPadder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(400, 300));

		createGui();
		pack();
	}

	private void createMenu() {

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewProfile = new JMenuItem("New Profile");
		mntmNewProfile.setActionCommand(ACTION_NEW_PROFILE);
		mntmNewProfile.addActionListener(this);
		mntmNewProfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNewProfile);

		JMenuItem mntmLoadProfile = new JMenuItem("Load Profile");
		mntmLoadProfile.setActionCommand(ACTION_LOAD_PROFILE);
		mntmLoadProfile.addActionListener(this);
		mntmLoadProfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmLoadProfile);
	}

	private void createGui() {
		createMenu();
		getContentPane().setLayout(new BorderLayout());

		profileTabPanel = new ApplicationTabPanel();
		profileTabPanel.addTabContainerListener(getTabListener());
		add(profileTabPanel, BorderLayout.CENTER);
	}

	private ContainerListener getTabListener() {
		return new ContainerAdapter() {
			@Override
			public void componentAdded(ContainerEvent e) {
				if (e.getChild() instanceof ControllerListener) {
					LOG.debug("Register new controller profile");
					ControllerEventQueue.addControllerListener((ControllerListener) e.getChild());
				}
			}

			@Override
			public void componentRemoved(ContainerEvent e) {
				if (e.getChild() instanceof ControllerListener) {
					LOG.debug("Remove controller profile");
					ControllerEventQueue.removeControllerListener((ControllerListener) e.getChild());
				}
			}
		};
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case ACTION_NEW_PROFILE:
			fireNewProfileEvent();
			break;
		case ACTION_LOAD_PROFILE:
			fireLoadProfileEvent();
			break;
		default:
			LOG.warn("Unknown cmd: '{}'", e.getActionCommand());
			break;
		}
	}

	private void fireLoadProfileEvent() {
		Padder padder = IO.load(null, this);
		if (padder == null) {
			return;
		}

		profileTabPanel.openTab(new ProfilePanel(padder), true);
	}

	private void fireNewProfileEvent() {
		profileTabPanel.openTab(new ProfilePanel(new ScriptPadder()), true);
	}

}
