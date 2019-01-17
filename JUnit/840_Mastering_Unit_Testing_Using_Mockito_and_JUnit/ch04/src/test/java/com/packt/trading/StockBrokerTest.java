package com.packt.trading;

import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.packt.trading.dto.Stock;

public class StockBrokerTest {
	MarketWatcher market;
	Portfolio portfolio;
	StockBroker broker;
	
	Stock global_stock;
	
	@BeforeEach
	void init() {
		market = mock(MarketWatcher.class);
		portfolio = mock(Portfolio.class);
		
		broker = new StockBroker(market);
		global_stock = when(mock(Stock.class).getPrice()).thenReturn(BigDecimal.ONE).getMock();
	}
	
	@Test
	void sanity() {
		assertNotNull(market);
		assertNotNull(portfolio);
	}
	
	@Test
	void market_Returns_current_stock_status() {
		Stock uvsityCorp = new Stock("UV", "UVSITY Corporation ", new BigDecimal(100));
		
		when(market.getQuote(anyString())).thenReturn(uvsityCorp);
		assertNotNull(market.getQuote("UV"));
	}
	
	
	@Test
	void when_ten_percent_gain_then_the_stock_is_sold() {
		Stock aCorp = new Stock("FB", "FaceBook", new BigDecimal(11.20));
		
		//Given a customer previously bought ‘FB’ stocks at $10.00/per share
		when(portfolio.getAvgPrice(isA(Stock.class))).thenReturn(BigDecimal.TEN);
		when(market.getQuote(eq(aCorp.getSymbol()))).thenReturn(aCorp);
		
		//when the ‘FB’ stock price becomes $11.00
		broker.perform(portfolio, aCorp);
		
		//then the ‘FB’ stocks  are sold
		verify(portfolio).sell(aCorp, 10);
	}
	
	@Test
	void zero_interaction() {
		verifyZeroInteractions(market, portfolio);
	}
	

	// This test will fail, this is to demonstrate the error
	@Test
	public void verify_no_more_interaction() {
		Stock noStock = null;
		portfolio.getAvgPrice(noStock);
		portfolio.sell(null, 0);
		verify(portfolio).getAvgPrice(eq(noStock));
		// this will fail as the sell method was invoked
		verifyNoMoreInteractions(portfolio);
	}
	
	@Test
	void argument_matcher() {
		when(portfolio.getAvgPrice(isA(Stock.class))).thenReturn(BigDecimal.TEN);
		
		Stock blueChipStock = new Stock("FB", "FB Corp", new BigDecimal(1000.00));
		Stock otherStock =    new Stock("XY", "XY Corp", new BigDecimal(5.00));
		
		when(market.getQuote(argThat(new StockSymbolMatcher(blueChipStock.getSymbol())))).thenReturn(blueChipStock);
		when(market.getQuote(argThat(new StockSymbolMatcher(otherStock.getSymbol())))).thenReturn(otherStock);
		
		broker.perform(portfolio, blueChipStock);
		verify(portfolio).sell(blueChipStock, 10);
		
		broker.perform(portfolio, otherStock);
		verify(portfolio, times(0)).sell(otherStock, 10);
	}
	
	@Test
	void throwsException() {
		when(portfolio.getAvgPrice(isA(Stock.class))).thenThrow(new IllegalStateException("Database down"));
		
		IllegalStateException e = assertThrows(IllegalStateException.class, () -> portfolio.getAvgPrice(global_stock));
		assertEquals("Database down", e.getMessage());
	}
	
	@Test
	void throwsException_void_methods() {
		doThrow(new IllegalStateException()).when(portfolio).buy(isA(Stock.class));
		
		IllegalStateException e = assertThrows(IllegalStateException.class, () -> portfolio.buy(new Stock(null, null, null)));
		assertNull(e.getMessage());
	}
	
	@Test
	void consecutive_calls() {
		Stock stock = new Stock(null, null, null);
		
		when(portfolio.getAvgPrice(stock)).thenReturn(BigDecimal.TEN, BigDecimal.ZERO);
		
		assertEquals(BigDecimal.TEN, portfolio.getAvgPrice(stock));
		assertEquals(BigDecimal.ZERO, portfolio.getAvgPrice(stock));
		assertEquals(BigDecimal.ZERO, portfolio.getAvgPrice(stock));
	}
	
	@Nested
	@SuppressWarnings("rawtypes")
	public class AnswerDemo {
		private Map<String, List<Stock>> stocksMap;
		
		@BeforeEach
		void init() {
			assertNull(stocksMap);
			stocksMap = new HashMap<>();
		}
		@AfterEach
		void clean() {
			stocksMap = null;
		}
		
		
		class BuyAnswer implements Answer {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Stock s = invocation.getArgument(0);
				return stocksMap.computeIfAbsent(s.getSymbol(), __ -> new ArrayList<>()).add(s);
			}
		}
		class TotalPriceAnswer implements Answer {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return stocksMap.values()
						.stream()
						.flatMap(List::stream)
						.map(Stock::getPrice)
						.filter(Objects::nonNull)
						.reduce(BigDecimal::add)
						.orElse(ZERO);
			}
		}

		
		@Test
		void answering() {
			assertEquals(0, stocksMap.size(), "stocksMap.size()");
			doAnswer(new BuyAnswer()).when(portfolio).buy(isA(Stock.class));
			when(portfolio.getCurrentValue()).then(new TotalPriceAnswer());
			
			portfolio.buy(new Stock("A", "A", BigDecimal.TEN));
			portfolio.buy(new Stock("B", "B", BigDecimal.ONE));
			
			assertEquals(BigDecimal.valueOf(11), portfolio.getCurrentValue());
		}
	}
	
	@Test
	void spying() throws Exception {
		Stock realStock = new Stock("A", "Company A", BigDecimal.ONE);
		Stock spyStock = spy(realStock);
		
        //call real method from  spy
        assertEquals("A", spyStock.getSymbol());
        
        //Changing value using spy
        spyStock.setPrice(BigDecimal.ZERO);
            
        //verify spy has the changed value
        assertEquals(BigDecimal.ZERO, spyStock.getPrice());
        
        //Stubbing method
        when(spyStock.getPrice()).thenReturn(BigDecimal.TEN);
        
        //Changing value using spy
        spyStock.setPrice(new BigDecimal("7"));
        
        //Stubbed method value 10.00  is returned NOT 7
        assertNotEquals(new BigDecimal("7") , spyStock.getPrice());
        
        //Stubbed method value 10.00
        assertEquals(BigDecimal.TEN,  spyStock.getPrice());
	}
	

	//This test will fail
	@Test
	public void doReturn_is_not_type_safe() throws Exception {
		//then return is type safe- It has to return a BigDecimal
		when(portfolio.getCurrentValue()).thenReturn(BigDecimal.ONE);
		//method call works fine
		portfolio.getCurrentValue();
		//returning a String instead of BigDecimal
		doReturn("See returning a String").when(portfolio.getCurrentValue());
		//this call will fail
		portfolio.getCurrentValue();
				
	}
	
	@Test
	public void doReturn_usage() throws Exception {
		List<String> list = new ArrayList<String>();
		List<String> spy = spy(list);
		
		//doReturn fixed the issue
		doReturn("now reachable").when(spy).get(0);
		assertEquals("now reachable", spy.get(0));
	}
	

	@Test
	public void argument_captor() throws Exception {
		when(portfolio.getAvgPrice(isA(Stock.class))).thenReturn(BigDecimal.TEN);
		Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
		when(market.getQuote(anyString())).thenReturn(aCorp);
		broker.perform(portfolio, aCorp);
		
		ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
		verify(market).getQuote(arg.capture());
		assertEquals("A", arg.getValue());
		
		ArgumentCaptor<Stock> arg0 = ArgumentCaptor.forClass(Stock.class);
		ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
		verify(portfolio).sell(arg0.capture(), arg1.capture());
		assertEquals("A", arg0.getValue().getSymbol());
		assertEquals(10, arg1.getValue().intValue());
	}
	
	@Test
	public void inorder() throws Exception {
		Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
		
		portfolio.getAvgPrice(aCorp);
		portfolio.getCurrentValue();
		market.getQuote("X");
		portfolio.buy(aCorp);
		
		InOrder inOrder=inOrder(portfolio,market);
		inOrder.verify(portfolio).getAvgPrice(isA(Stock.class));
		inOrder.verify(portfolio).getCurrentValue();
		inOrder.verify(market).getQuote(anyString());
		inOrder.verify(portfolio).buy(isA(Stock.class));
	}
	
	@Test
	public void changing_default() throws Exception {
		Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
		Portfolio pf = mock(Portfolio.class);
		//default null is returned
		assertNull(pf.getAvgPrice(aCorp));
		
		Portfolio pf1 = mock(Portfolio.class, RETURNS_SMART_NULLS);
		//a smart null is returned
		System.err.println("#1 "+pf1.getAvgPrice(aCorp));
		assertNotNull(pf1.getAvgPrice(aCorp));
		
		Portfolio pf2 = mock(Portfolio.class, RETURNS_MOCKS);
		//a smart null is returned
		System.err.println("#2 "+pf2.getAvgPrice(aCorp));
		assertNotNull(pf2.getAvgPrice(aCorp));
		
		Portfolio pf3 = mock(Portfolio.class, RETURNS_DEEP_STUBS);
		//a smart null is returned
		System.err.println("#3 "+pf3.getAvgPrice(aCorp));
		assertNotNull(pf3.getAvgPrice(aCorp));
	}
	
	@Test
	public void resetMock() throws Exception {
		Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
		
		Portfolio portfolio = mock(Portfolio.class);
		when(portfolio.getAvgPrice(eq(aCorp))).thenReturn(BigDecimal.ONE);
		assertNotNull(portfolio.getAvgPrice(aCorp));
		
		reset(portfolio);
		//Resets the stub, so getAvgPrice returns NULL
		assertNull(portfolio.getAvgPrice(aCorp));
	}
	
	@Test
	public void access_global_mock() throws Exception {
		assertEquals(BigDecimal.ONE, global_stock.getPrice());
	}
	
	@Test
	public void mocking_details() throws Exception {
		Portfolio pf1 = mock(Portfolio.class, RETURNS_MOCKS);
		
		BigDecimal result = pf1.getAvgPrice(global_stock);
		assertNotNull(result);
		assertTrue(mockingDetails(pf1).isMock());
		
		Stock myStock = new Stock(null, null, null);
		Stock spy = spy(myStock);
		assertTrue(mockingDetails(spy).isSpy());
		
	}
}

class StockSymbolMatcher implements ArgumentMatcher<String>{
	private final String symbol;
	public StockSymbolMatcher(String symbol) {
		this.symbol = symbol;
	}
	@Override
	public boolean matches(String argument) {
		return symbol.equals(argument);
	}
	
}