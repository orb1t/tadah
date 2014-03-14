/**
 * 
 */
package org.mdkt.datawiz;

import java.security.ProviderException;

/**
 * @author trung
 *
 */
public interface DataWizInstanceProvider {
	/**
	 * 
	 * @param handler
	 * @return not-null instance
	 * @throws ProviderException if can't provide an instance for a given handler
	 */
	public AbstractDataWiz get(Class<? extends AbstractDataWiz> handler);
}
