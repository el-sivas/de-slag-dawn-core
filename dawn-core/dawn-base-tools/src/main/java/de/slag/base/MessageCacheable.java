package de.slag.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface MessageCacheable extends Labelable {
	
	void append(String message);
	
	static MessageCacheable creating() {
		return new MessageCacheable() {
			
			private static final String NEWLINE = "\n";

			private static final String SEPARATOR = "| ";

			private Map<Long, String> messages = new HashMap<>();
			
			@Override
			public void append(String message) {
				synchronized (this) {
					messages.put(System.nanoTime(), message);
				}
			}

			@Override
			public String toString() {
				return generateString(SEPARATOR);
			}
			
			private String generateString(String separator) {
				final Collection<String> list = new ArrayList<>();
				messages.keySet().forEach(k -> list.add(messages.get(k)));
				return String.join(separator, list);
			}

			@Override
			public String getLabel() {
				return generateString(NEWLINE);
			}
		};
	}

}
