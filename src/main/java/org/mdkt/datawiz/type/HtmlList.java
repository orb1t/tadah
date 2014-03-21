/**
 *
 */
package org.mdkt.datawiz.type;

import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

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
		if (StringUtils.isBlank(input)) {
			return;
		}
		String[] values = input.split("<li>|</li>");
		if (values != null && values.length > 0) {
			for (String s : values) {
				if (StringUtils.isNotBlank(s)) {
					add(StringUtils.trim(StringEscapeUtils.unescapeHtml4(s)));
				}
			}
		}
	}

}
