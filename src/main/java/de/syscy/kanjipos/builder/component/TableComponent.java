package de.syscy.kanjipos.builder.component;

public class TableComponent extends TagComponent {
	public TableComponent() {
		super("table");
	}

	public static class TableRowComponent extends TagComponent {
		public TableRowComponent() {
			super("tr");
		}
	}

	public static class TableDataComponent extends TagComponent {
		public TableDataComponent() {
			super("td");
		}
	}
}