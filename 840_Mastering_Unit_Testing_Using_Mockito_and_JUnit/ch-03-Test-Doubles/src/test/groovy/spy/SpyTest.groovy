package spy

import static org.junit.Assert.*

import org.junit.Test
import org.mockito.Mockito

import com.pakt.trading.dto.Stock

class SpyTest {

	@Test
	public void spying() {
		Stock stock = new Stock('A', 'Company A', BigDecimal.ONE)
		Stock spy = Mockito.spy(stock)
		
		//call real method from spy
		assertSame stock.symbol, spy.symbol
		
		//Changing value using spy
		spy.price = 10
		
		//verify spy has the changed value
		assertEquals BigDecimal.TEN, spy.price
		
		//Stubbing method
		Mockito.when(spy.getPrice())
		.thenReturn(new BigDecimal(100))
		
		assertEquals 100 as BigDecimal, spy.price
	}

}
