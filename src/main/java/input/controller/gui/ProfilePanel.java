package input.controller.gui;

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
import input.controller.Constants;
import input.controller.logic.ControllerId;
import input.controller.logic.ControllerEventQueue.ControllerEvent;
import input.controller.logic.ControllerEventQueue.ControllerListener;
import input.controller.logic.padder.Padder;

public class ProfilePanel extends ApplicationTab implements ActionListener, ControllerListener {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ProfilePanel.class);

	private static final String ACTION_REFRESH = "refresh";
	private static final String ACTION_SAVE = "save";
	private static final String ACTION_EDIT = "edit";

	private Padder padder;

	private JComboBox<ControllerId> cbController;
	private DefaultComboBoxModel<ControllerId> cbControllerModel;

	private JPanel pnlButtons;
	private JPanel pnlDPad;
	private JPanel pnlAxis;

	public ProfilePanel(Padder padder) {

		this.padder = padder;

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
		btnSave.setIcon(new ImageIcon(InternalImage.loadFromPath(Constants.IMAGE_FOLDER, "save.png")));
		btnSave.setMnemonic('s');
		btnSave.setActionCommand(ACTION_SAVE);
		btnSave.addActionListener(this);
		toolBar.add(btnSave);

		JButton btnEdit = new JButton("Edit");
		btnEdit.setIcon(new ImageIcon(InternalImage.loadFromPath(Constants.IMAGE_FOLDER, "edit.png")));
		btnEdit.setMnemonic('r');
		btnEdit.setActionCommand(ACTION_EDIT);
		btnEdit.addActionListener(this);
		toolBar.add(btnEdit);

		JSeparator separator = new JSeparator();
		panel.add(separator, "2, 4, 3, 1");
		panel.add(cbController, "2, 6, fill, default");

		JButton btnRefresh = new JButton("");
		btnRefresh.setActionCommand(ACTION_REFRESH);
		btnRefresh.addActionListener(this);
		btnRefresh.setIcon(new ImageIcon(InternalImage.loadFromPath(Constants.IMAGE_FOLDER, "refresh.png")));
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
			SlidingControl axis = new SlidingControl(controller.getAxisValue(i), controller.getAxisName(i), "" + i);
			pnlAxis.add(axis);
		}

		pnlAxis.revalidate();
		pnlAxis.repaint();
	}

	private void updateDPad(Controller controller) {
		pnlDPad.removeAll();

		SlidingControl povX = new SlidingControl(controller.getPovX(), "PovX", "<N/A>");
		pnlDPad.add(povX);

		SlidingControl povY = new SlidingControl(controller.getPovY(), "PovY", "<N/A>");
		pnlDPad.add(povY);

		pnlDPad.revalidate();
		pnlDPad.repaint();
	}

	private void updateButtons(Controller controller) {
		pnlButtons.removeAll();

		int buttonCount = controller.getButtonCount();
		for (int i = 0; i < buttonCount; i++) {
			OnOffControl button = new OnOffControl(controller.isButtonPressed(i), controller.getButtonName(i), "" + i);
			pnlButtons.add(button);
		}

		pnlButtons.revalidate();
		pnlButtons.repaint();
	}

	private void updateButton(Controller controller, int index) {
		int count = pnlButtons.getComponentCount();
		if (index >= count) {
			updateButtons(controller);
			return;
		}

		OnOffControl contr = (OnOffControl) pnlButtons.getComponent(index);
		contr.setControlName(controller.getButtonName(index));
		contr.setIndicator(controller.isButtonPressed(index));
		contr.setId("" + index);
	}

	private void updateAxis(Controller controller, int index) {
		int count = pnlAxis.getComponentCount();
		if (index >= count) {
			updateAxis(controller);
			return;
		}

		SlidingControl axis = (SlidingControl) pnlAxis.getComponent(index);
		axis.setControlName(controller.getAxisName(index));
		axis.setValue(controller.getAxisValue(index));
		axis.setId("" + index);
	}

	private void fireControllerSelectionEvent() {
		setTitle(getSelectedController().toString());
		updateHardware();
	}

	private void fireSaveEvent() {
		IO.save(padder, this);
	}

	private void fireEditEvent() {
		Editor.edit(padder, this);
	}

	private void fireControllerEvent(final ControllerEvent event) {
		if (!isVisible()) {
			return;
		}

		final Controller source = event.getSource();

		boolean isEventDriven = false;
		if (event.isButtonEvent()) {
			isEventDriven = true;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateButton(source, event.getControlIndex());
				}
			});
			if (padder != null) {
				padder.translateButtonEvent(event, event.getControlIndex(),
						source.isButtonPressed(event.getControlIndex()));
			}
		}
		if (event.isPovEvent()) {
			isEventDriven = true;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateDPad(source);
				}
			});
			if (padder != null) {
				padder.translateDPadEvent(event, event.isxPovEvent(),
						(event.isxPovEvent()) ? source.getPovX() : source.getPovY());
			}
		}
		if (event.isAxisEvent() || event.isxAxisEvent() || event.isyAxisEvent()) {
			isEventDriven = true;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateAxis(source, event.getControlIndex());
				}
			});
			if (padder != null) {
				padder.translateAxisEvent(event, event.getControlIndex(), source.getAxisValue(event.getControlIndex()));
			}
		}

		if (!isEventDriven) {
			if (padder != null) {
				padder.pollEvent(event);
			}
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
		case ACTION_EDIT:
			fireEditEvent();
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
