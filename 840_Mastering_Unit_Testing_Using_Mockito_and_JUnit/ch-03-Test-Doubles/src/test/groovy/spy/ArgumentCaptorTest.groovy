package spy

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
@RunWith(MockitoJUnitRunner)
class ArgumentCaptorTest {
	@Mock MarketWatcher  marketWatcher
	@Mock Portfolio portfolio
	StockBroker broker;
	
		@Before
		void before() {
			broker = new StockBroker(marketWatcher)
		}
	
	@Test
	public void  argument_captor() {
		when(portfolio.getAvgPrice(isA(Stock)))
		.thenReturn(new BigDecimal(10))
		
		Stock stock = new Stock('UV', 'Uvsity Corporation', 11.20)
		
		when(marketWatcher.getQuote(anyString())).thenReturn(stock)
		broker.perform(portfolio, stock)
		
		ArgumentCaptor<String> stockSymbolCapture = ArgumentCaptor.forClass(String)
		
		verify(marketWatcher).getQuote(stockSymbolCapture.capture())
		
		assert stock.symbol == stockSymbolCapture.value 
		
		//Two arguments captured
		ArgumentCaptor<Stock> stockCapture = ArgumentCaptor.forClass(Stock)
		ArgumentCaptor<Integer> stockSellCountCapture = ArgumentCaptor.forClass(Integer)
		
		verify(portfolio).sell(stockCapture.capture(), stockSellCountCapture.capture())
		
		assert 'UV' == stockCapture.value.symbol
		assert 10 == stockSellCountCapture.value
	}
}
