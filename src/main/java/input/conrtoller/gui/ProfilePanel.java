package input.conrtoller.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.lwjgl.input.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import bno.swing2.widget.tab.ApplicationTab;
import game.engine.image.InternalImage;
import input.conrtoller.Constants;
import input.conrtoller.data.ControllerEventQueue.ControllerEvent;
import input.conrtoller.data.ControllerEventQueue.ControllerListener;
import input.conrtoller.data.ControllerId;

public class ProfilePanel extends ApplicationTab implements ActionListener, ControllerListener {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ProfilePanel.class);

	private static final String ACTION_REFRESH = "refresh";
	private static final String ACTION_SAVE = "save";
	private static final String ACTION_RESET = "reset";

	private JComboBox<ControllerId> cbController;
	private DefaultComboBoxModel<ControllerId> cbControllerModel;

	private JPanel pnlButtons;
	private JPanel pnlDPad;
	private JPanel pnlAxis;

	public ProfilePanel() {

		setTitle("undefined");
		createGui();
		updateControllers();
	}

	private void createGui() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		cbControllerModel = new DefaultComboBoxModel<ControllerId>();
		cbController = new JComboBox<ControllerId>(cbControllerModel);
		cbController.addItemListener(getControllerChangeListener());

		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		panel.add(toolBar, "2, 2, 3, 1");

		JButton btnSave = new JButton("Save");
		btnSave.setIcon(new ImageIcon(InternalImage.loadFromPath(Constants.imageFolder, "save.png")));
		btnSave.setMnemonic('s');
		btnSave.setActionCommand(ACTION_SAVE);
		btnSave.addActionListener(this);
		toolBar.add(btnSave);

		JButton btnReset = new JButton("Reset");
		btnReset.setIcon(new ImageIcon(InternalImage.loadFromPath(Constants.imageFolder, "reset.png")));
		btnReset.setMnemonic('r');
		btnReset.setActionCommand(ACTION_RESET);
		btnReset.addActionListener(this);
		toolBar.add(btnReset);

		JSeparator separator = new JSeparator();
		panel.add(separator, "2, 4, 3, 1");
		panel.add(cbController, "2, 6, fill, default");

		JButton btnRefresh = new JButton("");
		btnRefresh.setActionCommand(ACTION_REFRESH);
		btnRefresh.addActionListener(this);
		btnRefresh.setIcon(new ImageIcon(InternalImage.loadFromPath(Constants.imageFolder, "refresh.png")));
		btnRefresh.setToolTipText("Refresh");
		panel.add(btnRefresh, "4, 6");

		pnlButtons = new JPanel();
		pnlButtons.setBorder(new TitledBorder(null, "Buttons", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(pnlButtons, "2, 8, 3, 1, fill, top");
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));

		pnlDPad = new JPanel();
		pnlDPad.setBorder(new TitledBorder(null, "DPad", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(pnlDPad, "2, 10, 3, 1, fill, fill");
		pnlDPad.setLayout(new BoxLayout(pnlDPad, BoxLayout.Y_AXIS));

		pnlAxis = new JPanel();
		pnlAxis.setBorder(new TitledBorder(null, "Axis", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(pnlAxis, "2, 12, 3, 1, fill, fill");
		pnlAxis.setLayout(new BoxLayout(pnlAxis, BoxLayout.Y_AXIS));
	}

	public ControllerId getSelectedController() {
		return cbControllerModel.getElementAt(cbController.getSelectedIndex());
	}

	private void updateControllers() {
		cbControllerModel.removeAllElements();

		List<ControllerId> ids = ControllerId.getIds();
		for (ControllerId id : ids) {
			cbControllerModel.addElement(id);
		}

		cbController.revalidate();
		cbController.repaint();
		fireControllerSelectionEvent();
	}

	private void updateHardware() {
		Controller controller = getSelectedController().getController();
		updateButtons(controller);
		updateDPad(controller);
		updateAxis(controller);
	}

	private void updateAxis(Controller controller) {
		pnlAxis.removeAll();

		int axisCount = controller.getAxisCount();
		for (int i = 0; i < axisCount; i++) {
			SlidingControl axis = new SlidingControl(controller.getAxisValue(i), controller.getAxisName(i));
			pnlAxis.add(axis);
		}

		pnlAxis.revalidate();
		pnlAxis.repaint();
	}

	private void updateDPad(Controller controller) {
		pnlDPad.removeAll();

		pnlDPad.add(new JLabel(String.format("X -> %s", controller.getPovX())));
		pnlDPad.add(new JLabel(String.format("Y -> %s", controller.getPovY())));

		pnlDPad.revalidate();
		pnlDPad.repaint();
	}

	private void updateButtons(Controller controller) {
		pnlButtons.removeAll();

		int buttonCount = controller.getButtonCount();
		for (int i = 0; i < buttonCount; i++) {
			OnOffControl button = new OnOffControl(controller.isButtonPressed(i), controller.getButtonName(i));
			pnlButtons.add(button);
		}

		pnlButtons.revalidate();
		pnlButtons.repaint();
	}

	private void fireControllerSelectionEvent() {
		setTitle(getSelectedController().toString());
		updateHardware();
	}

	private void fireSaveEvent() {
		// TODO fireSaveEvent
	}

	private void fireResetEvent() {
		// TODO fireResetEvent
	}

	private void fireControllerEvent(ControllerEvent event) {
		final Controller source = event.getSource();

		if (event.isButtonEvent()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateButtons(source);
				}
			});
		}
		if (event.isPovEvent()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateDPad(source);
				}
			});
		}
		if (event.isAxisEvent() || event.isxAxisEvent() || event.isyAxisEvent()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateAxis(source);
				}
			});
		}
	}

	private ItemListener getControllerChangeListener() {
		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					fireControllerSelectionEvent();
				}
			}
		};
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case ACTION_REFRESH:
			updateControllers();
			break;
		case ACTION_SAVE:
			fireSaveEvent();
			break;
		case ACTION_RESET:
			fireResetEvent();
			break;
		default:
			LOG.warn("Unknown action: '{}'", e.getActionCommand());
			break;
		}
	}

	@Override
	public void action(ControllerEvent event) {
		ControllerId controller = getSelectedController();
		Controller source = event.getSource();
		if (source.getIndex() != controller.getId() || !source.getName().equals(controller.getName())) {
			return;
		}

		fireControllerEvent(event);
	}

}
