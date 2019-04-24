package de.syscy.kanjipos.data;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PosterEntry {
	public static final PosterEntry EMPTY = new PosterEntry() {
		@Override
		public String get(String key) {
			return ".";
		}
	};

	private final @Getter Map<String, String> data = new HashMap<>();

	public void set(String key, String value) {
		data.put(key, value);
	}

	public String get(String key) {
		return data.getOrDefault(key, "");
	}

	public boolean has(String key) {
		return data.containsKey(key);
	}
}