
import static org.junit.Assert.*

import com.manning.spock.chapter6.Basket
import com.manning.spock.chapter6.Product
import com.manning.spock.chapter6.mocks.BillableBasket
import com.manning.spock.chapter6.mocks.CreditCardProcessor
import com.manning.spock.chapter6.mocks.CreditCardResult
import com.manning.spock.chapter6.mocks.Customer
import com.manning.spock.chapter6.stubs.EnterprisyBasket
import com.manning.spock.chapter6.stubs.ServiceLocator
import com.manning.spock.chapter6.stubs.ShippingCalculator
import com.manning.spock.chapter6.stubs.WarehouseInventory

import spock.lang.Narrative
import spock.lang.Specification

class MocksAndStubSpec extends Specification {
    // Listing 6.25 Using mocks and stubs in the same test
    def 'card has no funds'() {
        given:'tv and camera as products'
        Product tv = new Product(name:"bravia",price:1200,weight:18)
        Product camera = new Product(name:"panasonic",price:350,weight:2)
        
        and:'a basket'
        BillableBasket basket = new BillableBasket()
        
        and:'a customer'
        Customer customer = new Customer(name:"John",vip:false,creditCard:"testCard") 

        and:'an fully stocked warehouse'
        WarehouseInventory inventory = Stub(WarehouseInventory) {
            isProductAvailable(_, _) >> true
            isEmpty() >> false
        }
        basket.warehouseInventory = inventory

        and:'a credit card service'
        CreditCardProcessor creditCardProcessor = Mock(CreditCardProcessor) 
        basket.creditCardProcessor = creditCardProcessor
        
        when:'user checks out two products'
        basket.addProduct tv
        basket.addProduct camera
        
        boolean charged = basket.fullCheckout(customer)  // trigger the tested action
        
        then:'nothing is charged if credit card does not have enough money'
        1 * creditCardProcessor.authorize(1550, customer) >> CreditCardResult.NOT_ENOUGH_FUNDS
        !charged  // Verify that nothing was charged.
        0 * _
    }

    // Listing 6.26 Verifying a sequence of events with interconnected method calls
    def 'happy path for credit card sale'() {
        given:'tv and camera as products'
        Product tv = new Product(name:"bravia",price:1200,weight:18)
        Product camera = new Product(name:"panasonic",price:350,weight:2)
        
        and:'a basket'
        BillableBasket basket = new BillableBasket()
        
        and:'a customer'
        Customer customer = new Customer(name:"John",vip:false,creditCard:"testCard")

        and:'a warehouse'
        WarehouseInventory inventory = Mock(WarehouseInventory)
        basket.warehouseInventory = inventory

        and:'a credit card service'
        CreditCardProcessor creditCardProcessor = Mock(CreditCardProcessor)
        basket.creditCardProcessor = creditCardProcessor
        CreditCardResult sampleResult = CreditCardResult.OK
        sampleResult.token = 'sample' 
        
        when:'user checks out two products'
        basket.addProduct tv
        basket.addProduct camera
        
        boolean charged = basket.fullCheckout(customer)  // trigger the tested action
        
        then:'credit card is checked'
        1 * creditCardProcessor.authorize(!null, 1) >> sampleResult
        
        then:'inventory is checked'
        with (inventory) {
            2 * isProductAvailable(!null, 1) >> true
            _ * isEmpty() >> false
        }
        
        then:'credit card is charged'
        1 * creditCardProcessor.capture({ myToken -> myToken.endsWith('sample') }, customer) >> CreditCardResult.OK
        charged
        0 * _
    }
    
    /**
     * This listing demonstrates several key points. First, this time the warehouse inventory is
     * a mock instead of a stub because you want to verify the correct calling of its methods.
     * You also want to verify that it gets non-null arguments.
     * Mocks and stubs support the with() Spock method that was introduced in chapter
     * 4. You’ve used it to group the two interactions of the warehouse inventory.
     * To verify that the basket class honors the token given back by the credit card processor, you create your own dummy token (named sample) and pass it to the basket
     * when the authorization step happens. You can then verify that the token handed to
     * the capture event is the same. Because the basket also prepends the token with the
     * date (which is obviously different each time the test runs), you have to use the endsWith() method in the Groovy closure that matches the token
     */
}
