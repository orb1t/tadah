/**
 * 
 */
package org.mdkt.datawiz.annotation;

import org.mdkt.datawiz.AbstractDataWiz;

/**
 * Data specification is loaded from a file
 *  
 * @author trung
 *
 */
public @interface TadahDataSpecFile {
	/**
	 * if not defined, test class name and method name would be used to construct the file name 
	 */
	String value() default "";
	
	/**
	 * data wiz handler for this "given" phrase
	 */
	Class<? extends AbstractDataWiz> handler();
}
