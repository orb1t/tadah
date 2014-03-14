/**
 * 
 */
package org.mdkt.datawiz;

/**
 * 
 * @author trung
 *
 */
public class DataWizException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6495494185335921770L;

	/**
	 * 
	 */
	public DataWizException() {
	}

	/**
	 * @param arg0
	 */
	public DataWizException(String message) {
		super(message);
	}

	/**
	 * @param arg0
	 */
	public DataWizException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DataWizException(String message, Throwable cause) {
		super(message, cause);
	}

}
