package mock

import static org.junit.Assert.*
import static org.mockito.ArgumentMatchers.*
import static org.mockito.Mockito.*

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer

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
class CustomAnswerTest {
	@Mock MarketWatcher  marketWatcher
	@Mock Portfolio portfolio

	Map<String, List<Stock>> stockMap = [:]

	Answer buyAnswer = { invocation ->
		Stock newStock = invocation.arguments[0]
		List<Stock> list = stockMap[newStock.symbol]
		if(!list) {
			list = []
			stockMap[newStock.symbol] = list
		}
		list << newStock
		null
	} as Answer
	
	Answer totalPriceAnswer = { invocation -> stockMap.values().flatten()*.price.sum() } as Answer

	@Test
	void answering() {
		stockMap.clear()
		doAnswer(buyAnswer)
		.when(portfolio).buy(isA(Stock))
		
		when(portfolio.getCurrentPrice())
		.then(totalPriceAnswer)
		
		portfolio.buy(new Stock("A", "A", BigDecimal.TEN))
		portfolio.buy(new Stock("B", "B", BigDecimal.ONE))
		
		assertEquals(new BigDecimal(11), portfolio.getCurrentPrice())
	}
}
