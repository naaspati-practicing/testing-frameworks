import static org.junit.Assert.*

import org.junit.After
import org.junit.AfterClass
import org.junit.Test

import spock.lang.Specification

class LifecycleSpec extends Specification {
    
    //@BeforeClass
    def setupSpec() {
        println 'setupSpec: will run only once, at start\n' 
    }
    //@AfterClass
    def cleanupSpec() {
        println 'cleanupSpec: will run only once, at end\n'
    }
    //@Before
    def setup() {
        println 'setup: Will run before EACH feature'
    }
    //@After
    def cleanup() {
        println 'cleanup: Will run after EACH feature\n'
    }
    def test1() {
        expect:'trivial test 1'
        println 'test-1'
        5 == 2 + 3
    }
    def test2() {
        expect:'trivial test 2'
        println 'test-2'
        5 == 2 + 3
    }
    def test3() {
        expect:'trivial test 3'
        println 'test-3'
        5 == 2 + 3
    }
}
    