

import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Logger

import spock.lang.Specification

abstract class LifeCycleDemoParentSpec extends Specification {
	private final static Logger logger = Logger.getLogger("Parent")
	protected final static AtomicInteger order = new AtomicInteger()
	
	protected void log(logger, methodname) {
		logger.info(String.format("%-15s%d", methodname, order.andIncrement))
	}

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
}
