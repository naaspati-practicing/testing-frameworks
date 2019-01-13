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
class ThrowingExceptionsTest {
	@Mock MarketWatcher  marketWatcher
	@Mock Portfolio portfolio
	
	@Test(expected = IllegalStateException)
	void testName() {
		when(portfolio.buy(isA(Stock)))
		.thenThrow(IllegalStateException)
		
		portfolio.buy(new Stock(null, null, null))
	}
}
