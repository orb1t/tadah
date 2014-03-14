/**
 * 
 */
package org.mdkt.datawiz.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.mdkt.datawiz.AbstractDataWiz;
import org.mdkt.datawiz.DataWizException;

/**
 * A model to encapsulate the data specification
 * 
 * @author trung
 *
 */
public class DataSpecModel {
	private Map<String, Object> context = new HashMap<String, Object>();
	private Stack<MethodSpecModel> methods = new Stack<MethodSpecModel>();
	
	/**
	 * @param varName
	 * @param actualValue
	 */
	public void addStaticBinding(String varName, Object actualValue) {
		Object currentValue = context.get(varName);
		if (currentValue != null && !actualValue.equals(currentValue)) {
			throw new DataWizException("tadah:var [" + varName + "] binds to 2 different values: [" + currentValue + "] and [" + actualValue + "]");
		}
		context.put(varName, actualValue);
	}
	
	/**
	 * @return the context
	 */
	public Map<String, Object> getContext() {
		return context;
	}
	
	/**
	 * @return the convertedMethods
	 */
	public List<MethodSpecModel> getMethods() {
		return methods;
	}
	/**
	 * 
	 */
	public void newMethodSpecModel() {
		methods.push(new MethodSpecModel());
	}

	/**
	 * @param handler
	 */
	public void setHandlerForCurrentMethodSpecModel(
			Class<? extends AbstractDataWiz> handler) {
		methods.peek().setHandler(handler);
	}

	/**
	 * @param methodName
	 */
	public void setMethodNameForCurrentMethodSpecModel(String methodName) {
		methods.peek().setMethodName(methodName);
	}

	/**
	 * @param varType
	 */
	public void addMethodArgumentTypeForCurrentMethodSpecModel(Class<?> varType) {
		methods.peek().addMethodArgumentType(varType);
	}

	/**
	 * @param actualValue
	 */
	public void addMethodArgumentValueForCurrentMethodSpecModel(
			Object actualValue) {
		methods.peek().addMethodArgumentValue(actualValue);
	}
}
