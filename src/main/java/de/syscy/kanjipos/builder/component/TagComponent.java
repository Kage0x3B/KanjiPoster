package de.syscy.kanjipos.builder.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;

@RequiredArgsConstructor
public abstract class TagComponent extends HtmlComponent {
	private final String tag;

	private @Getter @Setter String id;
	private List<String> classList = new ArrayList<>();
	private Map<String, String> attributes = new HashMap<>();

	public void addClass(String cssClass) {
		classList.add(cssClass);
	}

	public void addAttribute(String name, String content) {
		attributes.put(name, content);
	}

	@Override
	public void build(StringBuilder output) {
		output.append('<').append(tag);
		buildAttributes(output);
		output.append('>');

		buildChildren(output);

		output.append("</").append(tag).append('>');
		output.append('\n');
	}

	private void buildAttributes(StringBuilder output) {
		if(id != null && !id.isEmpty()) {
			output.append(" id='").append(id).append("'");
		}

		if(!classList.isEmpty()) {
			output.append(" class='");
			for(String cssClass : classList) {
				output.append(cssClass).append(' ');
			}
			output.append("'");
		}

		if(!attributes.isEmpty()) {
			for(Map.Entry<String, String> attribute : attributes.entrySet()) {
				output.append(' ').append(attribute.getKey()).append("='").append(attribute.getValue()).append("'");
			}
		}
	}
}