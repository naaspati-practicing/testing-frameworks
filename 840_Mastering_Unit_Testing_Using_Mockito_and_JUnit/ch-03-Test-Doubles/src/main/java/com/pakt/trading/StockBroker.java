package com.pakt.trading;

import java.math.BigDecimal;

import com.pakt.trading.dto.Stock;

public class StockBroker {
	private static final BigDecimal LIMIT = new BigDecimal(0.10);
	private final MarketWatcher marketWatcher;
	
	public StockBroker(MarketWatcher marketWatcher) {
		this.marketWatcher = marketWatcher;
	}
	public void perform(Portfolio portfolio, Stock stock) {
		Stock liveStock = marketWatcher.getQuote(stock.getSymbol());
		BigDecimal avgPrice = portfolio.getAvgPrice(stock);
		BigDecimal priceGained = liveStock.getPrice().subtract(avgPrice);
		BigDecimal percentGain = priceGained.divide(avgPrice);
		
		int compare = percentGain.compareTo(LIMIT);
		if(compare > 0)
			portfolio.sell(stock, 10);
		else if(compare < 0)
			portfolio.buy(stock);
	}
}
