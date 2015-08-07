package input.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import game.engine.time.TimeUtils;

public class Constants {
	public static final String imageFolder = "/images/";
	public static final long CONTROLLER_POLL_INTERVAL = TimeUtils.MILLISECONDS_PER_SECOND / 50;
	public static final String FILE_EXTENSION = ".pad";
	public static final Charset ENCODING = StandardCharsets.UTF_8;
}
