package mock

import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

import com.pakt.trading.MarketWatcher
import com.pakt.trading.Portfolio

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
class StubbingMethod_2_Test {
	@Mock MarketWatcher  marketWatcher
	@Mock Portfolio portfolio

	
	@Test
	public void  marketWatcher_Returns_current_stock_status() {
		Stock stock = new Stock('UV', 'Uvsity Corporation', new BigDecimal(100))
		
		Mockito.when(marketWatcher.getQuote(ArgumentMatchers.anyString()))
		.thenReturn(stock)
		
		assertSame(stock, marketWatcher.getQuote('UV'))
	}
}
