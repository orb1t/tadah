/**
 *
 */
package org.mdkt.datawiz;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author trung
 *
 */
public class DataSpecContentHandler implements ContentHandler {

	private static final Logger logger = Logger.getLogger(DataSpecContentHandler.class);

	private Stack<Boolean> tadahStack = new Stack<Boolean>();
	private Stack<DataSpecAttribute> attributeStack = new Stack<DataSpecAttribute>();
	private Stack<String> varStack = new Stack<String>();
	private Stack<Class<?>> varTypeStack = new Stack<Class<?>>();
	private Stack<StringBuffer> varValueStack = new Stack<StringBuffer>();
	private Stack<StringBuffer> methodNameStack = new Stack<StringBuffer>();


	private DataSpecVistor visitor;

	/**
	 * @param visitor
	 */
	public DataSpecContentHandler(DataSpecVistor visitor) {
		this.visitor = visitor;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (tadahStack.peek()) {
			switch (attributeStack.peek()) {
			case GIVEN:
				methodNameStack.peek().append(ch);
				break;
			case VAR:
				varValueStack.peek().append(ch);
				break;
			default:
				throw new SAXException("Unsupported attribute in the stack");
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (tadahStack.pop()) { // only for tadah attribute
			DataSpecAttribute current = attributeStack.pop();
			switch (current) {
			case GIVEN:
				String rawMethodName = methodNameStack.pop().toString();
				String actualMethodName = StringUtils.uncapitalize(WordUtils.capitalizeFully(rawMethodName.toString()).replaceAll("[^A-Za-z0-9_]", ""));
				logger.debug("Method name = [" + actualMethodName + "]");
				visitor.onGivenCompleted(actualMethodName);
				break;
			case VAR:
				String varName = varStack.pop();
				Class<?> varType = varTypeStack.pop();
				String rawVarValue = varValueStack.pop().toString();
				logger.debug("varName=[" + varName + "], varType=[" + varType + "], value=[" + rawVarValue + "]");
				Object varValue;
				try {
					varValue = varType.getConstructor(String.class).newInstance(rawVarValue);
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					throw new SAXException(e);
				}
				visitor.onVar(varName, varType, varValue);
				break;
			default:
			}
		} else {
			if (!attributeStack.isEmpty() && attributeStack.peek() == DataSpecAttribute.VAR) {
				varValueStack.peek().append("</").append(qName).append(">");
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		logger.debug("processingInstruction: target=[" + target + "] - data=[" + data + "]");
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	@Override
	public void setDocumentLocator(Locator locator) {

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	@Override
	public void skippedEntity(String name) throws SAXException {

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		logger.debug("start: uri=" + uri + ", localName=" + localName + ", qName=" + qName + ", attributes=" + atts);
		String var = atts.getValue(DataSpecAttribute.GIVEN.getQName());
		boolean isTadahAttribute = false;
		if (var != null) {
			// TODO some validation like: don't allow nested given or given inside var
			attributeStack.push(DataSpecAttribute.GIVEN);
			methodNameStack.push(new StringBuffer());
			visitor.onGiven();
			isTadahAttribute = true;
		} else { // it means i dont' expect to see other attribute in the tag which contains tadah:given
			// TODO some validation like: var name must not be empty, don't allow duplicated var, var without given in the current
			String varName = atts.getValue(DataSpecAttribute.VAR.getQName());
			String varType = atts.getValue(DataSpecAttribute.TYPE.getQName());
			if (StringUtils.isNotBlank(varName)) {
				attributeStack.push(DataSpecAttribute.VAR);
				isTadahAttribute = true;
				varStack.push(varName);
				varValueStack.push(new StringBuffer());
				try {
					varTypeStack.push(StringUtils.isBlank(varType) ? String.class : Class.forName(varType));
				} catch (ClassNotFoundException e) {
					throw new SAXException(e);
				}
			} else {
				if (!attributeStack.isEmpty() && attributeStack.peek() == DataSpecAttribute.VAR) {
					varValueStack.peek().append("<").append(qName).append(">");
				}
			}
		}
		tadahStack.push(isTadahAttribute);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {

	}

	/**
	 *
	 */
	public void reset() {
		attributeStack.clear();
		varStack.clear();
		varTypeStack.clear();
		varValueStack.clear();
		tadahStack.clear();
		methodNameStack.clear();
	}

}
