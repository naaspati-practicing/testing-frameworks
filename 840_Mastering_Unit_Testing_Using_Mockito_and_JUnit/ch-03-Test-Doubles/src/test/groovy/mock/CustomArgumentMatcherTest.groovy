package mock

import static org.junit.Assert.*
import static org.mockito.Mockito.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import com.pakt.trading.MarketWatcher
import com.pakt.trading.Portfolio
import com.pakt.trading.StockBroker
import com.pakt.trading.dto.Stock


@RunWith(MockitoJUnitRunner)
class CustomArgumentMatcherTest {

	@Mock MarketWatcher  marketWatcher
	@Mock Portfolio portfolio
	
	StockBroker broker;
	
	@Before
	void before() {
		broker = new StockBroker(marketWatcher)
	}

	//groovy
	ArgumentMatcher<String> blueChipMatcher = {symbol -> 'FB' == symbol || 'AAPL' == symbol } as ArgumentMatcher<String>

	/**
	 * same as
	 *  
	 * class BlueChipStockMatcher extends ArgumentMatcher<String>{
	 *   @Override
	 *   public boolean matches(Object symbol) {
	 *      return "FB".equals(symbol) || "AAPL".equals(symbol);
	 *   }
	 * }
	 * 
	 * blueChipMatcher = new BlueChipStockMatcher()
	 * 
	 */

	ArgumentMatcher<String> otherStockMatcher = {symbol -> !blueChipMatcher.matches(symbol)} as ArgumentMatcher<String>

	@Test
	void test() {
		when(portfolio.getAvgPrice(isA(Stock)))
				.thenReturn(new BigDecimal(10.00))

		Stock blueChipStock = new Stock("FB", "FB Corp", new BigDecimal(1000.00))
		Stock otherStock = new Stock("XY", "XY Corp", new BigDecimal(5.00))
		
		when(marketWatcher.getQuote(ArgumentMatchers.argThat(blueChipMatcher)))
		.thenReturn(blueChipStock)
		
		when(marketWatcher.getQuote(ArgumentMatchers.argThat(otherStockMatcher)))
		.thenReturn(otherStock)
		
		broker.perform(portfolio, blueChipStock)
		verify(portfolio).sell(blueChipStock, 10)
		
		broker.perform(portfolio, otherStock)
		verify(portfolio, never()).sell(otherStock, 10)
	}
}
