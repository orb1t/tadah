/**
 * 
 */
package org.mdkt.datawiz;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all data wiz implementation 
 * This will give some convenient methods to populate the test context with key,value which would be used later in the test scenario <br/>
 * This class is having thread local to store the variables and their values
 *  
 * @author trung
 * 
 * @since 1.0
 */
public class AbstractDataWiz {
	
	private ThreadLocal<Map<String, Object>> localContextThreadLocal = new ThreadLocal<Map<String,Object>>();
	
	public AbstractDataWiz() {
	}
	
	/**
	 * Persist the variable value into the local context
	 * 
	 * @param name
	 * @param value
	 */
	public void setValue(String name, Object value) {
		checkContext();
		localContextThreadLocal.get().put(name, value);
	}
	
	/**
	 * Verify if thread local has the map or not, if not then create an empty one
	 */
	private void checkContext() {
		if (localContextThreadLocal.get() == null) {
			localContextThreadLocal.set(new HashMap<String, Object>());
		}
	}

	/**
	 * 
	 * @return not-null map
	 */
	public Map<String, Object> getLocalContext() {
		checkContext();
		return localContextThreadLocal.get();
	}
}
