package com.pakt.trading;

import com.pakt.trading.dto.Stock;

public interface MarketWatcher {
	public Stock getQuote(String symbol);
}
