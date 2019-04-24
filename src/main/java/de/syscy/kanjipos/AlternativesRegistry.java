package de.syscy.kanjipos;

import org.joor.Reflect;

import java.security.InvalidAlgorithmParameterException;
import java.util.*;
import java.util.stream.Collectors;

public class AlternativesRegistry {
	private Map<Class<? extends IAlternative>, Map<String, Reflect>> alternativesMap = new HashMap<>();

	public void putAlternative(Class<? extends IAlternative> baseClass, Class<? extends IAlternative> alternativeClass) {
		if(!alternativesMap.containsKey(baseClass)) {
			alternativesMap.put(baseClass, new HashMap<>());
		}

		String name = alternativeClass.getSimpleName().toLowerCase();
		alternativesMap.get(baseClass).put(name, Reflect.on(alternativeClass));
	}

	public <T extends IAlternative> T createAlternative(Class<T> baseClass, String name, Object... args) {
		if(!alternativesMap.containsKey(baseClass)) {
			return null;
		}

		name = name.toLowerCase();

		Reflect alternativeClass = alternativesMap.get(baseClass).get(name);

		if(alternativeClass == null) {
			return null;
		}

		return alternativeClass.create(args).get();
	}

	public Set<String> getAlternativeNames(Class<? extends IAlternative> baseClass) {
		if(!alternativesMap.containsKey(baseClass)) {
			return Collections.emptySet();
		}

		return new HashSet<>(alternativesMap.get(baseClass).keySet());
	}

	public interface IAlternative {

	}
}