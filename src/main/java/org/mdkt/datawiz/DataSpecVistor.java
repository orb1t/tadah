/**
 *
 */
package org.mdkt.datawiz;

/**
 * Callback interface when parsing the data spec html
 *
 * @author trung
 *
 */
public interface DataSpecVistor {
	/**
	 * Invoked when first see tadah:given attribute
	 */
	public void onGiven();

	/**
	 * Invoked when complete parsing the tadah variable
	 *
	 * @param varName
	 * @param varType
	 * @param varValue value of type {@code varType}
	 */
	public void onVar(String varName, Class<?> varType, Object varValue);

	/**
	 * Invoked when given parsing completed
	 * @param actualMethodName
	 */
	public void onGivenCompleted(String actualMethodName);
}
