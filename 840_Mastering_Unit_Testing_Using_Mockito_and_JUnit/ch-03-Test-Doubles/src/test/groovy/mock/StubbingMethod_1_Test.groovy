package mock

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import static org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import static org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

import com.pakt.trading.MarketWatcher
import com.pakt.trading.Portfolio
import com.pakt.trading.StockBroker

import com.pakt.trading.dto.Stock

/**
 * The stubbing process defnes the
 * behavior of a mock method such as the value to be returned or the exception to be
 * thrown when the method is invoked.
 * The Mockito framework supports stubbing and allows us to return a given value
 * when a specifc method is called. This can be done using Mockito.when() along
 * with thenReturn ().
 *
 */
@RunWith(MockitoJUnitRunner)
class StubbingMethod_1_Test {
	@Mock MarketWatcher  marketWatcher
	@Mock Portfolio portfolio
	StockBroker broker;

	@Before
	void before() {
		broker = new StockBroker(marketWatcher)
	}

	@Test
	void when_ten_percent_gain_then_the_stock_is_sold() {
		//Portfolio's getAvgPrice is stubbed to return $10.00
		when(portfolio.getAvgPrice(isA(Stock)))
				.thenReturn(new BigDecimal(10))

		//A stock object is created with current price $11.20
		Stock aCorp = new Stock('A', 'A Corp', new BigDecimal(11.20))

		when(marketWatcher.getQuote(anyString()))
				.thenReturn(aCorp)

		//perform method is called, as the stock price increases
		// by 12% the broker should sell the stocks
		broker.perform(portfolio, aCorp)

		//verifying that the broker sold the stocks
		verify(portfolio).sell(aCorp, 10)
	}

	@Test
	void  marketWatcher_Returns_current_stock_status() {
		Stock stock = new Stock('UV', 'Uvsity Corporation', new BigDecimal(100))

		when(marketWatcher.getQuote(anyString()))
				.thenReturn(stock)

		assertSame(stock, marketWatcher.getQuote('UV'))
	}

	@Test 
	void verify_no_more_interaction() {
		Stock noStock = null;
		portfolio.getAvgPrice(noStock);
		portfolio.sell(null, 0);
		verify(portfolio).getAvgPrice(eq(noStock));
		//this will fail as the sell method was invoked
		verifyNoMoreInteractions(portfolio);
	}
	
	
}
