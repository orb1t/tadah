/**
 *
 */
package org.mdkt.datawiz.model;


import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mdkt.datawiz.AbstractDataWiz;
import org.mdkt.datawiz.DataSpecAttribute;
import org.mdkt.datawiz.DataSpecParser;
import org.mdkt.datawiz.DataSpecVistor;
import org.mdkt.datawiz.DataWizException;
import org.mdkt.datawiz.annotation.TadahDataSpec;
import org.mdkt.datawiz.annotation.TadahDataSpecFile;
import org.mdkt.datawiz.annotation.TadahDataSpecList;

/**
 * @author trung
 *
 */
public class DataSpecModelBuilder {

	private static final Logger logger = Logger.getLogger(DataSpecModelBuilder.class);

	private final DataSpecModel model = new DataSpecModel();;
	private DataSpecParser parser = null;
	private Class<? extends AbstractDataWiz> currentHandler = null;

	/**
	 *
	 */
	private DataSpecModelBuilder() {
		parser = new DataSpecParser(new DataSpecVistor() {

			@Override
			public void onVar(String varName, Class<?> varType, Object varValue) {
				model.addStaticBinding(varName, varValue);
				model.addMethodArgumentTypeForCurrentMethodSpecModel(varType);
				model.addMethodArgumentValueForCurrentMethodSpecModel(varValue);
			}

			@Override
			public void onGivenCompleted(String actualMethodName) {
				model.setMethodNameForCurrentMethodSpecModel(actualMethodName);
				model.setHandlerForCurrentMethodSpecModel(currentHandler);
			}

			@Override
			public void onGiven() {
				model.newMethodSpecModel();
			}
		});
	}

	public static DataSpecModelBuilder create() {
		return new DataSpecModelBuilder();
	}

	public DataSpecModelBuilder from(TadahDataSpec spec) {
		if (spec != null) {
			parse(spec);
		}
		return this;
	}

	/**
	 * @param tadahDataSpecs
	 */
	private void parse(TadahDataSpec... specs) {
		if (specs != null && specs.length > 0) {
			for (TadahDataSpec spec : specs) {
				currentHandler = spec.handler();
				try {
					parser.parse(new StringBuffer()
						.append("<div ").append(DataSpecAttribute.GIVEN.getQName()).append(">")
						.append(spec.value()).append("</div>").toString());
				} catch (Exception e) {
					throw new DataWizException(e);
				}
			}
		}
	}

	public DataSpecModelBuilder from(TadahDataSpecList spec) {
		if (spec != null) {
			parse(spec.value());
		}
		return this;
	}

	/**
	 * if there's no value in the spec annotation, construct the spec html file name based on test class and method name<br/>
	 * E.g.: TestClass.test_method.html<br/>
	 * the file has to reside in the same folder as the TestClass
	 * @param spec
	 * @param testMethod
	 * @return
	 */
	public DataSpecModelBuilder from(TadahDataSpecFile spec, Method testMethod) {
		if (spec != null) {
			model.newMethodSpecModel();
			String specFile = (StringUtils.isNotBlank(spec.value())) ? spec.value()
					: new StringBuffer().append(testMethod.getDeclaringClass().getSimpleName())
						.append(".").append(testMethod.getName()).append(".html").toString();
			logger.debug("Data Spec file: " + specFile);
			currentHandler = spec.handler();
			try {
				parser.parse(testMethod.getDeclaringClass().getResourceAsStream(specFile));
			} catch (Exception e) {
				throw new DataWizException(e);
			}
		}
		return this;
	}

	public DataSpecModel build() {
		return model;
	}
}
