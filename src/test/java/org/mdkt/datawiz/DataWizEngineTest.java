/**
 *
 */
package org.mdkt.datawiz;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mdkt.datawiz.annotation.TadahDataSpec;
import org.mdkt.datawiz.annotation.TadahDataSpecFile;
import org.mdkt.datawiz.annotation.TadahDataSpecList;
import org.mdkt.datawiz.type.ListType;

/**
 * @author trung
 *
 */
public class DataWizEngineTest {

	@Rule
	public TestName testName = new TestName();

	@Before
	public void before() {
		DataWizEngine.instance().prepare(this.getClass(), testName.getMethodName());
	}

	@After
	public void after() {
		DataWizEngine.instance().cleanUp();
	}

	@TadahDataSpecList({
		@TadahDataSpec(
			value="Create <span tadah:var='countUser' tadah:type='java.lang.Integer'>3</span> users",
			handler=DummyDataWiz.class
		),
		@TadahDataSpec(
			value="User <span tadah:var='userName' tadah:type='java.lang.String'>John</span> logs in",
			handler=DummyDataWiz.class
		),
		@TadahDataSpec(
			value="Deposit <span tadah:var='currency'>USD</span> <span tadah:var='amount' tadah:type='java.math.BigDecimal'>100000.00</span> into <span tadah:var='accountId'>12345</span>",
			handler=DummyDataWiz.class
		),
		@TadahDataSpec(
			value="User <span tadah:var='userName' tadah:type='java.lang.String'>John</span> logs in",
			handler=DummyDataWiz.class
		)
	})
	@Test
	public void static_binding_with_primitive_types() {
		verifyContextVariable("countUser", Integer.class, 3);
		verifyContextVariable("userName", String.class, "John");
		verifyContextVariable("amount", BigDecimal.class, new BigDecimal("100000.00"));
		verifyContextVariable("accountId", String.class, "12345");
	}

	@TadahDataSpec(
		value="User <span tadah:var='userName'>Wendy</span> logs in",
		handler=DummyDataWiz.class
	)
	@Test
	public void static_binding_with_default_type() {
		verifyContextVariable("userName", String.class, "Wendy");
	}

	@TadahDataSpecFile(handler=DummyDataWiz.class)
	@Test
	public void data_spec() {
		verifyContextVariable("countProduct", Integer.class, 3);
		verifyContextVariable("productList", 1, "Laptop");
		verifyContextVariable("productList", 2, "Mobile");
		verifyContextVariable("productList", 3, "Music Player");
	}

	/**
	 *
	 * @param varName
	 * @param idx
	 * @param expectedValue
	 */
	private void verifyContextVariable(String varName, int idx, String expectedValue) {
		Object contextValue = DataWizEngine.instance().getContextValue(varName);
		Assert.assertNotNull("Context static-binding variable [" + varName + "] must not be null", contextValue);
		Assert.assertEquals("Context static-binding variable [" + varName + "] data type", ListType.class, contextValue.getClass());
		Assert.assertEquals("Context static-binding variable [" + varName + "] value at index [" + idx + "]", expectedValue, ((ListType)contextValue).get(idx));
	}

	/**
	 * @param varName
	 * @param expectedType
	 * @param expectedValue
	 */
	private <T> void verifyContextVariable(String varName, Class<T> expectedType,
			T expectedValue) {
		Object contextValue = DataWizEngine.instance().getContextValue(varName);
		Assert.assertNotNull("Context static-binding variable [" + varName + "] must not be null", contextValue);
		Assert.assertEquals("Context static-binding variable [" + varName + "] data type", expectedType, contextValue.getClass());
		Assert.assertEquals("Context static-binding variable [" + varName + "] value", expectedValue, contextValue);
	}

	@TadahDataSpecList({
		@TadahDataSpec(
			value="Account <span tadah:var='acountId'>12345</span> is in credit",
			handler=DummyDataWiz.class
		),
		@TadahDataSpec(
			value="User <span tadah:var='userName'>Mary</span> made no withdrawals recently",
			handler=DummyDataWiz.class
		)
	})
	@Test
	public void static_binding_mutliple_values() {

	}

}