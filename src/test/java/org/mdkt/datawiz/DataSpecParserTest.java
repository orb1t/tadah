/**
 *
 */
package org.mdkt.datawiz;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;
import org.mdkt.datawiz.type.ListType;

/**
 * @author trung
 *
 */
public class DataSpecParserTest {
	@Test
	public void parse_simple() throws Exception {
		DataSpecVistor visitor = EasyMock.createMock(DataSpecVistor.class);
		visitor.onGiven();
		EasyMock.expectLastCall().once();
		visitor.onGivenCompleted(EasyMock.eq("depositInto"));
		EasyMock.expectLastCall().once();
		visitor.onVar(EasyMock.eq("currency"), EasyMock.eq(String.class), EasyMock.eq("USD")); EasyMock.expectLastCall();
		visitor.onVar(EasyMock.eq("amount"), EasyMock.eq(BigDecimal.class), EasyMock.eq(new BigDecimal("100000.00"))); EasyMock.expectLastCall();
		visitor.onVar(EasyMock.eq("accountId"), EasyMock.eq(String.class), EasyMock.eq("12345")); EasyMock.expectLastCall();
		EasyMock.replay(visitor);
		new DataSpecParser(visitor)
			.parse("<div tadah:given>Deposit <span tadah:var='currency'>USD</span> <span tadah:var='amount' tadah:type='java.math.BigDecimal'>100000.00</span> into <span tadah:var='accountId'>12345</span></div>");
		EasyMock.verify(visitor);
	}

	@Test
	public void parse_html() throws Exception {
		DataSpecVistor visitor = EasyMock.createMock(DataSpecVistor.class);
		visitor.onGiven();
		EasyMock.expectLastCall().once();
		visitor.onGivenCompleted(EasyMock.eq("givenProducts"));
		EasyMock.expectLastCall().once();
		visitor.onVar(EasyMock.eq("countProduct"), EasyMock.eq(Integer.class), EasyMock.eq(3)); EasyMock.expectLastCall();
		Capture<ListType> captureValue = new Capture<ListType>();
		visitor.onVar(EasyMock.eq("productList"), EasyMock.eq(ListType.class), EasyMock.capture(captureValue)); EasyMock.expectLastCall();
		EasyMock.replay(visitor);
		new DataSpecParser(visitor).parse(this.getClass().getResourceAsStream(this.getClass().getSimpleName() + ".html"));
		ListType productList = captureValue.getValue();
		Assert.assertEquals("Number of products", 3, productList.size());
		Assert.assertEquals("productList[0]", "Laptop", productList.get(0));
		Assert.assertEquals("productList[1]", "Mobile", productList.get(1));
		Assert.assertEquals("productList[2]", "Music Player", productList.get(2));
		EasyMock.verify(visitor);
	}
}
