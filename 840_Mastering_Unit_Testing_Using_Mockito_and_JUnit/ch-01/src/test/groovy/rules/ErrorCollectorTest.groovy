package rules

import static org.junit.Assert.*

import static org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ErrorCollector

/**
 * 
 * The ErrorCollector rule allows the execution of a test to continue after the frst
 * problem is found (for example, to collect all the incorrect rows in a table and report
 * them all at once) 
 *
 */
class ErrorCollectorTest {
	
	@Rule
	public ErrorCollector collector = new ErrorCollector()

	@Test
	public void test() {
		def ct = collector.&checkThat
		ct('a', equalTo('b') )
		ct(1, equalTo(2) )
		ct('ae', equalTo('g'))
	}
}
