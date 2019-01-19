

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.logging.Logger

class LifeCycleDemoSpec extends LifeCycleDemoParentSpec {
	private final static Logger logger = Logger.getLogger("Child ");
	
	def setupSpec() {
		log logger, "setupSpec()"
	}
	def cleanupSpec() {
		log logger, "cleanupSpec()"
	}
	def setup() {
		log logger, "setup()"
	}
	def cleanup() {
		log logger, "cleanup()"
	}
	
	def 'just to invoke setup() cleanup()' () {
		log logger, "test_invoke"
		
		expect:
		true
	}
}
