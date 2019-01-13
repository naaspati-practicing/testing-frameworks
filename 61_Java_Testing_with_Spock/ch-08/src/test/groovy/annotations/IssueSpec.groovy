package annotations
import static org.junit.Assert.*

import com.manning.spock.chapter8.eshop.BillableBasket
import com.manning.spock.chapter8.eshop.CreditCardProcessor
import com.manning.spock.chapter8.eshop.Customer
import com.manning.spock.chapter8.eshop.Product
import com.manning.spock.chapter8.eshop.WarehouseInventory

import spock.lang.Issue
import spock.lang.Specification
import spock.lang.Timeout

class IssueSpec extends Specification {
    @Issue("Bad List access")  // This test method verifies the fix that happened for issue 'Bad List access'.
    // Listing 8.4 Marking a test method with the issue it solves
    def 'Bad acceess of list'(){
        given:'a list'
        def list = []

        when:'accesing a index out of bounds'
        def value = list.get(4)

        then:'expect out of bound error'
        thrown(IndexOutOfBoundsException)
    }

    // Listing 8.5 Using the URL of an issue solved by a Spock test
    @Issue("http://redmine.example.com/issues/2554")
    def "Error conditions for unknown products - better"() {
        given: "a warehouse"
        WarehouseInventory inventory = new WarehouseInventory()
        when: "warehouse is queried for the wrong product"
        inventory.isProductAvailable("productThatDoesNotExist",1)
        then: "an exception should be thrown"
        IllegalArgumentException e = thrown()
        e.getMessage() == "Uknown product productThatDoesNotExist"
    }

    // Listing 8.6 Marking a Spock test with multiple issues
    @Issue(["JIRA-453", "JIRA-678", "JIRA-3485"])
    def "Negative quantity is the same as 0"() {
        given: "a warehouse"
        WarehouseInventory inventory = new WarehouseInventory()
        and: "a product"
        Product tv = new Product(name:"bravia",price:1200,weight:18)
        when: "warehouse is loaded with a negative value"
        inventory.preload(tv,-5)
        then: "the stock is empty for that product"
        notThrown(IllegalArgumentException)
        !inventory.isProductAvailable(tv.getName(),1)
    }

}
