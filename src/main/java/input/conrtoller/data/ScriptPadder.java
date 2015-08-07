package input.conrtoller.data;

import input.conrtoller.data.ControllerEventQueue.ControllerEvent;

//TODO ScriptPadder
public class ScriptPadder implements Padder {

	private String script;

	public ScriptPadder() {
		this("");
	}

	public ScriptPadder(String script) {
		this.setScript(script);
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public void translateButtonEvent(ControllerEvent event, int id, boolean pressed) {
	}

	@Override
	public void translateDPadEvent(ControllerEvent event, boolean xAxis, float value) {
	}

	@Override
	public void translateAxisEvent(ControllerEvent event, int id, float value) {
	}

}
