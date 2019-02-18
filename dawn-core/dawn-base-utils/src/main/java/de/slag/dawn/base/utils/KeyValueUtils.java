package de.slag.dawn.base.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class KeyValueUtils {

	private static final String KV_SEPARATOR = "=";

	private static final String MAIN_SEPARATOR = ";";

	private static final Function<String, Map<String, String>> SPLITTER = new Function<String, Map<String, String>>() {

		@Override
		public Map<String, String> apply(String string) {
			final List<String> asList = Arrays.asList(string.split(MAIN_SEPARATOR));
			final Map<String, String> map = new HashMap<>();

			asList.forEach(kv -> {
				String[] split = kv.split(KV_SEPARATOR);
				String key = split[0];
				String value = split[1];
				map.put(key, value);
			});

			return map;
		}
	};

	private static final Function<Map<String, String>, String> JOINER = new Function<Map<String, String>, String>() {

		@Override
		public String apply(Map<String, String> t) {
			final Collection<String> properties = new ArrayList<>();
			final Set<String> keySet = t.keySet();
			keySet.forEach(key -> {
				String value = t.get(key);
				properties.add(key + KV_SEPARATOR + value);
			});
			return String.join(MAIN_SEPARATOR, properties);
		}
	};
	
	public static String join(Map<String, String> t) {
		return JOINER.apply(t);
	}
	
	public static Map<String, String> split(String t) {
		return SPLITTER.apply(t);
	}

}
