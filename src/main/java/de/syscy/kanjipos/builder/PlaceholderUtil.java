package de.syscy.kanjipos.builder;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.function.Function;

@UtilityClass
public class PlaceholderUtil {
	public static final String PLACEHOLDER_PREFIX = "{{";
	public static final String PLACEHOLDER_SUFFIX = "}}";

	public static void processPlaceholders(StringBuilder stringBuilder, Collection<String> keys, Function<String, String> valueFunction) {
		processPlaceholders(stringBuilder, keys, valueFunction, PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX);
	}

	public static void processPlaceholders(StringBuilder stringBuilder, Collection<String> keys, Function<String, String> valueFunction, String prefix, String suffix) {
		for(String propertyName : keys) {
			String propertyReplaceMarker = prefix + propertyName + suffix;

			int markerIndex = stringBuilder.indexOf(propertyReplaceMarker);

			if(markerIndex != -1) {
				String propertyValue = valueFunction.apply(propertyName);

				stringBuilder.replace(markerIndex, markerIndex + propertyReplaceMarker.length(), propertyValue);
			}
		}
	}
}