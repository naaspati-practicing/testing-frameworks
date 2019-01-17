package com.packt.trading;

import static org.mockito.Mockito.mock;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import com.packt.trading.dto.Stock;

public class StockBrokerBDDTest {
	MarketWatcher market;
	Portfolio portfolio;
	StockBroker broker;
	
	@BeforeEach
	void init() {
		market = mock(MarketWatcher.class);
		portfolio = mock(Portfolio.class);
		
		broker = new StockBroker(market);
	}
	
	@Test
	void should_sell_a_stock_when_price_increases_by_ten_percent() {
		Stock aCorp = new Stock("FB", "FaceBook", new BigDecimal(11.20));
		
		//Given a customer previously bought ‘FB’ stocks at $10.00/per share
		given(portfolio.getAvgPrice(isA(Stock.class))).willReturn(BigDecimal.valueOf(10));
		given(market.getQuote(eq(aCorp.getSymbol()))).willReturn(aCorp);
		
		//when the ‘FB’ stock price becomes $11.00
		broker.perform(portfolio, aCorp);
		
		//then the ‘FB’ stocks  are sold
		verify(portfolio).sell(aCorp, 10);
	}
}
