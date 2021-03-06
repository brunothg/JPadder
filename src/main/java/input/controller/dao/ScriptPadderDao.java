package input.controller.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.script.ScriptException;

import input.controller.Constants;
import input.controller.logic.padder.ScriptPadder;

public class ScriptPadderDao {

	private Path path;

	public ScriptPadderDao(Path path) {
		this.path = path;
	}

	public void store(ScriptPadder padder) throws IOException {
		String script = padder.getScript();
		Files.write(path, script.getBytes(Constants.ENCODING));
	}

	public ScriptPadder load() throws IOException {
		String script = new String(Files.readAllBytes(path), Constants.ENCODING);
		try {
			return new ScriptPadder(script);
		} catch (ScriptException e) {
			throw new IOException(e);
		}
	}
}
