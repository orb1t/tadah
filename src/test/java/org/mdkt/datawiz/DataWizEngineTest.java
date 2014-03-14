/**
 * 
 */
package org.mdkt.datawiz;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mdkt.datawiz.annotation.TadahDataSpec;

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
	
	@TadahDataSpec(
		value="User <span tadah:var='userName' tadah:type='java.lang.String'>John</span> logs in",
		handler=DummyDataWiz.class
	)
	@Test
	public void static_binding_with_string_type() {
		Object contextValue = DataWizEngine.instance().getContextValue("userName");
		Assert.assertNotNull("Context static-binding variable must not be null", contextValue);
		Assert.assertEquals("Context static-binding variable data type", String.class, contextValue.getClass());
		Assert.assertEquals("Context static-binding variable 'userName'", "John", contextValue);
	}
	
	@TadahDataSpec(
			value="User <span tadah:var='userName'>John</span> logs in",
			handler=DummyDataWiz.class
		)
	@Test
	public void static_binding_with_default_type() {
		Object contextValue = DataWizEngine.instance().getContextValue("userName");
		Assert.assertNotNull("Context static-binding variable must not be null", contextValue);
		Assert.assertEquals("Context static-binding variable default data type", String.class, contextValue.getClass());
		Assert.assertEquals("Context static-binding variable 'userName'", "John", contextValue);
	}
	
}