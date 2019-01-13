import static org.junit.Assert.*

import com.manning.spock.chapter8.eshop.WarehouseInventory

import spock.lang.Issue
import spock.lang.Specification

class ExceptionsSpec extends Specification {

    // Listing 8.1 Expecting an exception in a Spock test
    def 'expecting errors'(){
        given:'a list'
        def list = []

        when:'accesing a index out of bounds'
        def fail = list.get(10)

        then:'expect out of bound error'
        thrown(IndexOutOfBoundsException)
    }

    // Listing 8.2 Detailed examination of an expected exception
    def 'checking error msg '(){
        given:'a list'
        def list = []

        when:'accesing a index out of bounds'
        def fail = list.get(10)

        then:'expect out of bound error'
        IndexOutOfBoundsException e = thrown()
        e.message == 'Index: 10, Size: 0'
    }

    // Listing 8.3 Explicit declaration that an exception shouldn’t happen
    def 'expecting error should not happen'(){
        given:'a list'
        def list = [1, 2, 3, 4, 5]

        when:'accesing a index out of bounds'
        def value = list.get(4)

        then:'expect out of bound error'
        notThrown(IndexOutOfBoundsException)
        value == 5
    }
}
