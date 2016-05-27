package input.controller.logic;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class ControllerId {

	private Controller controller;

	public ControllerId(Controller controller) {
		this.controller = controller;
	}

	public String getName() {
		return controller.getName();
	}

	public int getId() {
		return controller.getIndex();
	}

	public Controller getController() {
		return this.controller;
	}

	@Override
	public String toString() {
		return String.format("%d: %s", getId(), getName());
	}

	public static List<ControllerId> getIds() {
		int controllerCount = Controllers.getControllerCount();
		List<ControllerId> ids = new ArrayList<ControllerId>(controllerCount);
		for (int i = 0; i < controllerCount; i++) {
			ids.add(new ControllerId(Controllers.getController(i)));
		}

		return ids;
	}

}
