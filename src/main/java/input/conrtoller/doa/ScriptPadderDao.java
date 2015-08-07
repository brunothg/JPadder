package input.conrtoller.doa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import input.conrtoller.Constants;
import input.conrtoller.data.ScriptPadder;

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
		return new ScriptPadder(script);
	}
}
