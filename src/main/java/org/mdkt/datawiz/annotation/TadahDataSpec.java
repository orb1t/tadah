/**
 * 
 */
package org.mdkt.datawiz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mdkt.datawiz.AbstractDataWiz;

/**
 * Method annotated would be instrumented and corresponding data wiz will be executed
 * 
 * @author trung
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TadahDataSpec {
	/**
	 * The 'given' phrase in HTML<br/>
	 * E.g.: {@code User <span tadah:var="userName">John</span> logs in}
	 */
	String value();
	
	/**
	 * data wiz handler for this "given" phrase
	 */
	Class<? extends AbstractDataWiz> handler();
}
