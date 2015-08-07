package input.controller.data;

import java.awt.AWTException;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bno.swing2.utils.Null;
import input.controller.Constants;
import input.controller.data.ControllerEventQueue.ControllerEvent;
import input.controller.util.IOUtils;

public class ScriptPadder implements Padder {

	private static final Logger LOG = LoggerFactory.getLogger(ScriptPadder.class);
	private static final String FUNCTION_NAME = "controllerEvent";

	private static final ScriptEngineManager factory = new ScriptEngineManager();
	private static Robot robot;

	private static Object getRobot() {
		if (robot == null) {
			try {
				robot = new Robot();
			} catch (AWTException e) {
				LOG.warn("No robot available", e);
			}
		}

		return robot;
	}

	private static String getDefaultScript() {
		try {
			return IOUtils.readFully(ScriptPadder.class.getResourceAsStream("/defaultScript.js"), Constants.ENCODING);
		} catch (IOException e) {
			LOG.warn("Could not load default script", e);
		}
		return "";
	}

	private String scriptString;
	private ScriptEngine engine;

	public ScriptPadder() {
		try {
			this.setScript(getDefaultScript());
		} catch (ScriptException e) {
			LOG.error("Scripting error in default script", e);
		}
	}

	public ScriptPadder(String script) throws ScriptException {
		this.setScript(script);
	}

	public String getScript() {
		return scriptString;
	}

	public void setScript(String script) throws ScriptException {
		script = Null.nvl(script, "");
		getEngine().eval(script);

		this.scriptString = script;
	}

	@Override
	public void translateButtonEvent(ControllerEvent event, int id, boolean pressed) {
		try {
			runScript(event);
		} catch (ScriptException e) {
			LOG.warn("Script error", e);
		}
	}

	@Override
	public void translateDPadEvent(ControllerEvent event, boolean xAxis, float value) {
		try {
			runScript(event);
		} catch (ScriptException e) {
			LOG.warn("Script error", e);
		}
	}

	@Override
	public void translateAxisEvent(ControllerEvent event, int id, float value) {
		try {
			runScript(event);
		} catch (ScriptException e) {
			LOG.warn("Script error", e);
		}
	}

	private ScriptEngine getEngine() throws ScriptException {
		if (engine == null) {
			engine = Null.nvl(factory.getEngineByName("rhino"), factory.getEngineByMimeType("text/javascript"));
			engine.put("console", LOG);
			engine.put("robot", getRobot());
			engine.eval("function " + FUNCTION_NAME + "(event){console.debug('Function not implemented');}");
		}
		return engine;
	}

	public void runScript(ControllerEvent event) throws ScriptException {

		ScriptEngine engine = getEngine();
		Invocable inv = (Invocable) engine;

		try {
			inv.invokeFunction(FUNCTION_NAME, event);
		} catch (NoSuchMethodException e) {
			throw new ScriptException(e);
		}
	}

}
