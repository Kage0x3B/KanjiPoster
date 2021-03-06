package de.syscy.kanjipos.gui.property.types;

import de.syscy.kanjipos.gui.property.PropertiesEditorConfig;

/**
 * Property type description which can be used in the editor. This interface
 * defines the meta data of these types and conversion methods between Java
 * types and the corresponding nodes used within the editor.
 * 
 * @author Philipp Katz
 * @param <TYPE>
 *            The JavaType which corresponds with this property type.
 * @see PropertyTypes
 */
public interface PropertyType<TYPE> {

	/**
	 * @return <code>true</code> in case this is a collection type (i.e.
	 *         contains children).
	 */
	boolean isCollection();

	/**
	 * @return The default value of the property. <code>null</code> for
	 *         collection types.
	 */
	TYPE getDefaultValue();

	/**
	 * @return The underlying Java type.
	 */
	Class<? super TYPE> getType();

	/**
	 * Converts a Java object to a property node.
	 * 
	 * @param key
	 *            (optional) key.
	 * @param object
	 *            The object.
	 * @param config
	 *            The configuration.
	 * @return The property node.
	 */
	PropertyNode fromObject(String key, Object object, PropertiesEditorConfig config);

	/**
	 * Converts a PropertyNode to a Java object.
	 * 
	 * @param propertyNode
	 *            The property node.
	 * @return The Java object.
	 */
	TYPE toObject(PropertyNode propertyNode);
}
