package de.syscy.kanjipos.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SimplePosterData implements IPosterData {
	private final @Getter List<PosterEntry> entries = new ArrayList<>();
}