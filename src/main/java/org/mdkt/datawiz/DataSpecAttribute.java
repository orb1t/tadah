/**
 *
 */
package org.mdkt.datawiz;

/**
 * @author trung
 *
 */
public enum DataSpecAttribute {
	GIVEN("tadah:given"), VAR("tadah:var"), TYPE("tadah:type");

	private String qName;

	DataSpecAttribute(String qName) {
		this.qName = qName;
	}

	/**
	 * @return the qName
	 */
	public String getQName() {
		return qName;
	}
}
