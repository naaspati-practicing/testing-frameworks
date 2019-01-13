package rules

import static org.junit.Assert.*

import org.junit.Rule
import org.junit.Test
import org.junit.rules.Verifier

/**
 * Verifer is a base class of ErrorCollector, which can otherwise turn passing tests
* into failing tests if a verifcation check fails. 
 *
 */
class VerifierTest {
	String errorMsg = null
	
	@Rule
	public Verifier rule = [verify: {assertNull('ErrorMsg should be null after each test execution', errorMsg)}] as Verifier
	
	/* this is same as 
	 * 
	 *  public TestRule rule = new Verifier() {
	 *    protected void verify() {
	 *      assertNull("ErrorMsg should be null after each test execution",errorMsg);
	 *    }  
	 *  };
	 * 
	 */
	@Test
	public void test() {
		errorMsg = 'I have a dream'
	}
}
