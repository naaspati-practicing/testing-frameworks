package mock

import static org.junit.Assert.*

import org.junit.Test

import com.pakt.trading.MarketWatcher
import com.pakt.trading.Portfolio

import static org.mockito.Mockito.mock

/**
 * creating mock using Mockito.mock
 * @author Sameer
 *
 */
class CreatingMock_1_Test {
	
	MarketWatcher  marketWatcher = mock(MarketWatcher)
	Portfolio portfolio = mock(Portfolio)
}
