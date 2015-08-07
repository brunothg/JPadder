package input.conrtoller.doa;

import java.nio.file.Path;

import input.conrtoller.data.ScriptPadder;

//TODO ScriptPadderDao
public class ScriptPadderDao {

	private Path path;

	public ScriptPadderDao(Path path) {
		this.path = path;
	}

	public void store(ScriptPadder padder) {
	}

	public ScriptPadder load() {
		return null;
	}
}
