package de.syscy.kanjipos.builder.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TextComponent extends HtmlComponent {
	private final @Getter String text;

	@Override
	public void build(StringBuilder output) {
		output.append(text);
	}
}