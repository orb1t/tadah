/**
 * 
 */
package org.mdkt.datawiz.model;

import java.util.ArrayList;
import java.util.List;

import org.mdkt.datawiz.AbstractDataWiz;

/**
 * @author trung
 *
 */
public class MethodSpecModel {
	
	private Class<? extends AbstractDataWiz> handler;
	private String methodName;
	private List<Class<?>> methodArgumentTypes = new ArrayList<Class<?>>();
	private List<Object> methodArgumentValues = new ArrayList<Object>();
	
	
	/**
	 * @param handler
	 */
	public void setHandler(Class<? extends AbstractDataWiz> handler) {
		this.handler = handler;
	}

	/**
	 * @return the handler
	 */
	public Class<? extends AbstractDataWiz> getHandler() {
		return handler;
	}

	/**
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	
	/**
	 * @return the methodArgumentTypes
	 */
	public List<Class<?>> getMethodArgumentTypes() {
		return methodArgumentTypes;
	}
	
	/**
	 * @return the methodArgumentValues
	 */
	public List<Object> getMethodArgumentValues() {
		return methodArgumentValues;
	}
	
	/**
	 * @param varType
	 */
	public void addMethodArgumentType(Class<?> varType) {
		this.methodArgumentTypes.add(varType);
	}

	/**
	 * @param actualValue
	 */
	public void addMethodArgumentValue(Object actualValue) {
		this.methodArgumentValues.add(actualValue);
	}
}
