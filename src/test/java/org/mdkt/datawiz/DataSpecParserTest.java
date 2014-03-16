/**
 *
 */
package org.mdkt.datawiz;

import org.junit.Test;

/**
 * @author trung
 *
 */
public class DataSpecParserTest {
	@Test
	public void parse_simple() throws Exception {
		new DataSpecParser(new DataSpecVistor() {

			@Override
			public void onGiven() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onVar(String varName, Class<?> varType, Object varValue) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGivenCompleted(String actualMethodName) {
				// TODO Auto-generated method stub

			}


		}).parse("<div tadah:given>Deposit <span tadah:var='currency'>USD</span> <span tadah:var='amount' tadah:type='java.math.BigDecimal'>100000.00</span> into <span tadah:var='accountId'>12345</span></div>");
	}

	@Test
	public void parse_html() throws Exception {
		new DataSpecParser(new DataSpecVistor() {

			@Override
			public void onVar(String varName, Class<?> varType, Object varValue) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGivenCompleted(String actualMethodName) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGiven() {
				// TODO Auto-generated method stub

			}
		}).parse(this.getClass().getResourceAsStream(this.getClass().getSimpleName() + ".html"));
	}
}
