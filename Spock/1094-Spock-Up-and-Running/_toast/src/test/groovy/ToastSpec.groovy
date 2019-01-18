import org.spockframework.runtime.HamcrestFacade

import spock.lang.Specification

class ToastSpec extends Specification {
	
	  def "hello world"() {
		  System.err.println('System.err.println("")');
		  
		  expect: 'hello world'
		  10 < System.currentTimeMillis()
	  }
}
