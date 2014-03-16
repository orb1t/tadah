/**
 *
 */
package org.mdkt.datawiz;

import java.security.ProviderException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author trung
 *
 */
public class DefaultDataWizInstanceProvider implements DataWizInstanceProvider {

	private Map<Class<? extends AbstractDataWiz>, AbstractDataWiz> dataWizInstance = new HashMap<Class<? extends AbstractDataWiz>, AbstractDataWiz>();

	/**
	 * @param handler
	 */
	private AbstractDataWiz addHandler(Class<? extends AbstractDataWiz> handler) {
		if (!dataWizInstance.containsKey(handler)) {
			try {
				dataWizInstance.put(handler, handler.newInstance());
			} catch (Exception e) {
				throw new ProviderException(e);
			}
		}
		return dataWizInstance.get(handler);
	}

	/* (non-Javadoc)
	 * @see org.mdkt.datawiz.DataWizInstanceProvider#get(java.lang.Class)
	 */
	@Override
	public AbstractDataWiz get(Class<? extends AbstractDataWiz> handler) {
		return addHandler(handler);
	}

}
