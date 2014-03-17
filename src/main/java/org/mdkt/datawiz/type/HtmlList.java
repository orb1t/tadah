/**
 *
 */
package org.mdkt.datawiz.type;

import java.util.ArrayList;

/**
 * Support {@code <li> tag}. E.g.:
 * {@code <ul tadah:var='list' tadah:type='org.mdkt.datawiz.type.HtmlList'><li>V1</li><li>V2</li></ul>}
 *
 * @author trung
 *
 */
public class HtmlList extends ArrayList<String> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5906803247465524798L;

	/**
	 * Input is a string contains series of {@code <li> tags}. Need to parse and populate the value in the array list
	 */
	public HtmlList(String input) {

	}

}
