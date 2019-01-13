

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import static org.mockito.ArgumentMatchers.*

import org.mockito.ArgumentCaptor
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
class ChangingDefaultSettings {
	@Test
	public void  changing_default() {
		Stock stock = new Stock('UV', 'Uvsity Corporation', 11.20)

		Portfolio pf = mock(Portfolio)
		// defult null is returned
		assertNull pf.getAvgPrice(stock)

		Portfolio pf1 = mock(Portfolio, RETURNS_SMART_NULLS)
		// a smart null is returned
		println "#1 ${pf1.getAvgPrice(stock)}"
		assertNotNull pf1.getAvgPrice(stock)

		Portfolio pf2 = mock(Portfolio, RETURNS_MOCKS)
		// a mock is returned
		println "#2 ${pf2.getAvgPrice(stock)}"
		assertNotNull pf2.getAvgPrice(stock)

		Portfolio pf3 = mock(Portfolio, RETURNS_DEEP_STUBS)
		// a deep mock is returned
		println "#3 ${pf3.getAvgPrice(stock)}"
		assertNotNull pf3.getAvgPrice(stock)
	}
}
