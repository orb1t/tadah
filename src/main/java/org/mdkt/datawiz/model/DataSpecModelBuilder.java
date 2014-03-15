/**
 *
 */
package org.mdkt.datawiz.model;


import java.io.StringReader;
import java.lang.reflect.Method;

import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.sax.HtmlParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.mdkt.datawiz.DataWizException;
import org.mdkt.datawiz.annotation.TadahDataSpec;
import org.mdkt.datawiz.annotation.TadahDataSpecFile;
import org.mdkt.datawiz.annotation.TadahDataSpecList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author trung
 *
 */
public class DataSpecModelBuilder {

	private static final Logger logger = Logger.getLogger(DataSpecModelBuilder.class);

	private final DataSpecModel model = new DataSpecModel();;
	private HtmlParser parser = null;

	/**
	 *
	 */
	private DataSpecModelBuilder() {
		parser = new HtmlParser();
		parser.setXmlnsPolicy(XmlViolationPolicy.ALLOW);
		parser.setNamePolicy(XmlViolationPolicy.ALLOW);
		parser.setContentHandler(new ContentHandler() {
			private String varName = null;
			private Class<?> varType = null;
			private StringBuffer varValue = new StringBuffer();
			private StringBuffer methodName = new StringBuffer();

			public void startPrefixMapping(String prefix, String uri)
					throws SAXException {
				// TODO Auto-generated method stub

			}

			public void startElement(String uri, String localName, String qName,
					Attributes atts) throws SAXException {
				logger.debug("start: uri=" + uri + ", localName=" + localName + ", qName=" + qName + ", attributes=" + atts);
				if (atts.getValue("tadah:given") != null) {
					methodName.append("_");
				} else if (StringUtils.isNotBlank(methodName)){
					String _varName = atts.getValue("tadah:var");
					if (StringUtils.isNotBlank(varName)) {
						throw new SAXException("Potential duplicate tadah:var attribute");
					}
					if (StringUtils.isBlank(_varName)) {
						throw new SAXException("tadah:var must have some value");
					}
					varName = _varName;
					varValue.delete(0, varValue.length());
					String type = atts.getValue("tadah:type");
					if (type == null) {
						varType = String.class;
					} else {
						try {
							varType = Class.forName(type);
						} catch (ClassNotFoundException e) {
							throw new SAXException(e);
						}
					}
				}
			}

			public void startDocument() throws SAXException {
				// TODO Auto-generated method stub

			}

			public void skippedEntity(String name) throws SAXException {
				// TODO Auto-generated method stub

			}

			public void setDocumentLocator(Locator locator) {
				// TODO Auto-generated method stub

			}

			public void processingInstruction(String target, String data)
					throws SAXException {
				// TODO Auto-generated method stub

			}

			public void ignorableWhitespace(char[] ch, int start, int length)
					throws SAXException {
				// TODO Auto-generated method stub

			}

			public void endPrefixMapping(String prefix) throws SAXException {
				// TODO Auto-generated method stub

			}

			public void endElement(String uri, String localName, String qName)
					throws SAXException {
				logger.debug("end: uri=" + uri + ", localName=" + localName + ", qName=" + qName);
				if (StringUtils.isNotEmpty(varName)) {
					try {
						Object actualValue = varType.getConstructor(String.class).newInstance(varValue.toString());
						model.addStaticBinding(varName, actualValue);
						model.addMethodArgumentTypeForCurrentMethodSpecModel(varType);
						model.addMethodArgumentValueForCurrentMethodSpecModel(actualValue);
					} catch (Exception e) {
						throw new SAXException(e);
					}
					varName = null;
					varValue.delete(0, varValue.length());
				} else if (StringUtils.isNotBlank(methodName)) {
					methodName.deleteCharAt(0);
					String actualMethodName = StringUtils.uncapitalize(WordUtils.capitalizeFully(methodName.toString()).replaceAll("\\s", ""));

					logger.debug("Method name: " + actualMethodName);
					model.setMethodNameForCurrentMethodSpecModel(actualMethodName);
					methodName.delete(0, methodName.length());
				}
			}

			public void endDocument() throws SAXException {
				// TODO Auto-generated method stub

			}

			public void characters(char[] ch, int start, int length)
					throws SAXException {
				if (StringUtils.isNotBlank(varName)) {
					varValue.append(new String(ch));
				} else if (StringUtils.isNotBlank(methodName)) {
					methodName.append(" ").append(StringUtils.trim(new String(ch)));
				}
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
				model.newMethodSpecModel();
				try {
					parser.parse(new InputSource(new StringReader("<div tadah:given>" + spec.value() + "</div>")));
				} catch (Exception e) {
					throw new DataWizException(e);
				}
				model.setHandlerForCurrentMethodSpecModel(spec.handler());
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
			try {
				parser.parse(new InputSource(testMethod.getDeclaringClass().getResourceAsStream(specFile)));
			} catch (Exception e) {
				throw new DataWizException(e);
			}
			model.setHandlerForCurrentMethodSpecModel(spec.handler());
		}
		return this;
	}

	public DataSpecModel build() {
		return model;
	}
}
