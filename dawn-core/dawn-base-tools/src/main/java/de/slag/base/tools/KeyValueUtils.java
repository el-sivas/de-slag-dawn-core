package de.slag.base.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyValueUtils {

	/**
	 * pattern: 'key=value' per line
	 * 
	 * @param br
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> read(BufferedReader br) throws IOException {
		final Map<String, String> map = new HashMap<>();
		while (true) {
			final String readLine = br.readLine();
			if (readLine == null) {
				break;
			}
			final String[] split = readLine.split("=");
			map.put(split[0], split[1]);
		}
		return map;
	}

}
