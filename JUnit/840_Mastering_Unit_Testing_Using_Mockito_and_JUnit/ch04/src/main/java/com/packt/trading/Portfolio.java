package com.packt.trading;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.packt.trading.dto.Stock;

public class Portfolio {
	private final Map<String, List<Stock>>  stocksMap = new HashMap<>();

	public void buy(Stock stock) {
		stocksMap.computeIfAbsent(stock.getSymbol(), __ -> new ArrayList<>()).add(stock);
	}

	public void sell(Stock stock, int quentity) {
		List<Stock> stocks = stocksMap.get(stock.getSymbol());

		if(stocks == null)
			throw new IllegalStateException(stock.getSymbol()+" not bought");

		stocks.subList(0, Math.min(quentity, stocks.size())).clear();
	}
	public BigDecimal getAvgPrice(Stock s){
		List<Stock> stocks =  stocksMap.getOrDefault(s.getSymbol(), Collections.emptyList());

		if(stocks.isEmpty())
			return ZERO;

		return stocks.stream()
				.map(Stock::getPrice)
				.filter(Objects::nonNull)
				.reduce(BigDecimal::add)
				.map(b -> b.divide(valueOf(stocks.size())))
				.orElse(ZERO);
	}
	public BigDecimal getCurrentValue() {
		if(stocksMap.isEmpty())
			return ZERO;

		return stocksMap.values()
				.stream()
				.flatMap(List::stream)
				.map(Stock::getPrice)
				.filter(Objects::nonNull)
				.reduce(BigDecimal::add)
				.orElse(ZERO);
	}

}
