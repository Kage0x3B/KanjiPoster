package de.syscy.kanjipos.builder.component;

import java.util.LinkedList;
import java.util.List;

public abstract class HtmlComponent {
    private List<HtmlComponent> children = new LinkedList<>();

    public HtmlComponent addText(String text) {
        return add(new TextComponent(text));
    }

    public HtmlComponent add(HtmlComponent child) {
        children.add(child);

        return this;
    }

    protected void buildChildren(StringBuilder output) {
        for (HtmlComponent child : children) {
            child.build(output);
        }
    }

    public abstract void build(StringBuilder output);
}