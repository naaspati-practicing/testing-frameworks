package com.packt.trading;

import java.math.BigDecimal;

import com.packt.trading.dto.Stock;

public class StockBroker {
	public final static BigDecimal LIMIT = new BigDecimal(0.10);
	
	private final MarketWatcher market;

	public StockBroker(MarketWatcher market) {
		this.market = market;
	}
	
	void perform(Portfolio portfolio, Stock stock) {
		Stock live_Stock  = market.getQuote(stock.getSymbol());
		BigDecimal avg = portfolio.getAvgPrice(stock);
		BigDecimal gained = live_Stock.getPrice().subtract(avg);
		BigDecimal percent_gain = gained.divide(avg);
		
		int compared  = percent_gain.compareTo(LIMIT);
		if(compared > 0)
			portfolio.sell(stock, 10);
		else if(compared < 0)
			portfolio.buy(stock);
	}
	

}
