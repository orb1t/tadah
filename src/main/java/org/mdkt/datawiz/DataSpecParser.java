/**
 *
 */
package org.mdkt.datawiz;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.sax.HtmlParser;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author trung
 *
 */
public class DataSpecParser {
	private HtmlParser parser;
	private DataSpecContentHandler contentHandler;

	public DataSpecParser(final DataSpecVistor visitor) {
		contentHandler = new DataSpecContentHandler(visitor);
		parser = new HtmlParser();
		parser.setXmlnsPolicy(XmlViolationPolicy.ALLOW);
		parser.setNamePolicy(XmlViolationPolicy.ALLOW);
		parser.setContentHandler(contentHandler);
	}

	public void parse(InputStream is) throws IOException, SAXException{
		parse(new InputSource(is));
	}

	public void parse(String s) throws IOException, SAXException {
		parse(new InputSource(new StringReader(s)));
	}

	/**
	 * @param inputSource
	 * @throws Exception
	 */
	private void parse(InputSource inputSource) throws IOException, SAXException {
		contentHandler.reset();
		parser.parse(inputSource);
	}
}
