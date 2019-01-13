import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import static org.mockito.ArgumentMatchers.*

import org.mockito.ArgumentCaptor
import org.mockito.InOrder
import org.mockito.Mock
import static org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

import com.pakt.trading.MarketWatcher
import com.pakt.trading.Portfolio
import com.pakt.trading.StockBroker
import com.pakt.trading.dto.Stock

@RunWith(MockitoJUnitRunner)
class InvocationOrderTest {
	@Mock MarketWatcher  marketWatcher
	@Mock Portfolio portfolio
	StockBroker broker;
	
		@Before
		void before() {
			broker = new StockBroker(marketWatcher)
		}
	
	@Test
	void  failed_test() {
		Stock stock = new Stock('UV', 'Uvsity Corporation', 11.20)
		portfolio.getAvgPrice(stock)
		portfolio.getCurrentPrice()
		marketWatcher.getQuote('X')
		portfolio.buy(stock)
		
		InOrder inOrder = inOrder(portfolio, marketWatcher)
		
		inOrder.verify(portfolio).buy(isA(Stock))
		inOrder.verify(portfolio).getAvgPrice(isA(Stock))
	}
	// Reordering the verifcation sequence
	@Test
	void  passed_test() {
		Stock stock = new Stock('UV', 'Uvsity Corporation', 11.20)
		portfolio.getAvgPrice(stock)
		portfolio.getCurrentPrice()
		marketWatcher.getQuote('X')
		portfolio.buy(stock)
		
		InOrder inOrder = inOrder(portfolio, marketWatcher)
		
		inOrder.verify(portfolio).getAvgPrice(isA(Stock))
		inOrder.verify(portfolio).getCurrentPrice()
		inOrder.verify(marketWatcher).getQuote(anyString())
		inOrder.verify(portfolio).buy(isA(Stock))
	}
}
