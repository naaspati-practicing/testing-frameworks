package com.pakt.trading;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.pakt.trading.dto.Stock;

public class Portfolio {
	private final Map<String, List<Stock>> stockMap = new HashMap<>();

	public void buy(Stock stock) {
		List<Stock> list = stockMap.get(stock.getSymbol());
		if(list == null)
			stockMap.put(stock.getSymbol(), list = new ArrayList<>());

		list.add(stock);
	}
	public void sell(Stock stock, int quantity) {
		List<Stock> list = stockMap.get(stock.getSymbol());
		if(list == null)
			throw new IllegalStateException(stock.getSymbol()+" no bought");

		if(list.size() < quantity)
			list.subList(quantity, list.size()).clear();
	}
	public BigDecimal getAvgPrice(Stock stock) {
		return 
				Optional.ofNullable(stockMap.get(stock.getSymbol()))
				.map(list -> sum(list.stream()).divide(new BigDecimal(list.size())))
				.orElse(BigDecimal.ZERO);
	}
	public BigDecimal sum(Stream<Stock> stream) {
		return stream.reduce(BigDecimal.ZERO, (sum, stock2) -> sum.add(stock2.getPrice()), BigDecimal::add);
	}
	public BigDecimal getCurrentPrice() {
		return sum(stockMap.values().stream().flatMap(List::stream));
	}
}
