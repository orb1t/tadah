/**
 *
 */
package org.mdkt.datawiz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * List of {@link TadahDataSpec}
 *
 * @author trung
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TadahDataSpecList {
	TadahDataSpec[] value();
}
