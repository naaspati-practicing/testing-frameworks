package mock

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.configuration.MockAnnotationProcessor

import com.pakt.trading.MarketWatcher
import com.pakt.trading.Portfolio

import static org.mockito.Mockito.mock

/**
 * creating mock using @Mock
 * 
 * However, to work with the @Mock annotation, you are required to call
 * MockitoAnnotations.initMocks( this ) before using the mocks,
 * or use MockitoJUnitRunner as a JUnit runner.
 * 
 */
class CreatingMock_2_Test {
	
	@Mock
	MarketWatcher  marketWatcher
	@Mock
	Portfolio portfolio
	
	@Before
	void setUp() {
		MockitoAnnotations.initMocks(this)
	}
	
	@Test
	void sanity() {
		assertNotNull(marketWatcher)
		assertNotNull(portfolio)
	}
}
