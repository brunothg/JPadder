package input.conrtoller.data;

import input.conrtoller.data.ControllerEventQueue.ControllerEvent;

public interface Padder {
	public void translateButtonEvent(ControllerEvent event, int id, boolean pressed);

	public void translateDPadEvent(ControllerEvent event, boolean xAxis, float value);

	public void translateAxisEvent(ControllerEvent event, int id, float value);
}
