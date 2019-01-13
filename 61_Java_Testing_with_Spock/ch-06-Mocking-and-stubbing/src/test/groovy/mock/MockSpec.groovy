package mock
import static org.junit.Assert.*

import com.manning.spock.chapter6.Product
import com.manning.spock.chapter6.mocks.BillableBasket
import com.manning.spock.chapter6.mocks.CreditCardProcessor
import com.manning.spock.chapter6.mocks.Customer
import com.manning.spock.chapter6.stubs.WarehouseInventory

import spock.lang.Specification

class MockSpec extends Specification {
    Product tv, camera
    WarehouseInventory warehouseInventory
    BillableBasket basket
    Customer customer
    CreditCardProcessor creditCardProcessor

    def setup() {
        given:'a tv and a camera'
        tv = new Product(name:"bravia",price:1200,weight:18)
        camera = new Product(name:"panasonic",price:350,weight:2)

        and:'a basket'
        basket = new BillableBasket()

        and:'a customer'
        customer = new Customer(name:"John",vip:false, creditCard:"testCard")

        and:'an empty warehouse'
        warehouseInventory = Mock(WarehouseInventory)  // create spock stub
        basket.warehouseInventory = warehouseInventory

        and:'a credit card processor'
        creditCardProcessor = Mock(CreditCardProcessor)
        basket.creditCardProcessor = creditCardProcessor

    }

    // Listing 6.13 Stubbing mocks
    def 'if warehouse is empty nothing can be shipped'(){
        given:'when basket checks inventory status '
        warehouseInventory.isEmpty() >> true

        when:'user checks out tv'
        basket.addProduct tv

        then:'order cannot be shipped'
        !basket.canShipCompletely()
    }


    // Listing 6.15 Verification of a mocked method
    def 'credit card connection is always closed down'(){

        when:'user checks out the tv'
        basket.addProduct tv
        basket.checkout customer

        then:'connection is always closed at the end'
        1 * creditCardProcessor.shutdown()  //Verifying the called method
    }

    // 6.3.3 Verifying order of interactions
    //Listing 6.16 Verification of a specific order of mocked methods
    def 'credit card connection is closed down in the end '(){

        when:'user checks out the tv'
        basket.addProduct tv
        basket.checkout customer

        then:'credit card is charged and'
        1 * creditCardProcessor.sale(_, _) // First this verification will be checked

        then:'the credit card service is closed'
        1 * creditCardProcessor.shutdown()  // This will only be checked if the first verification passes.
    }

    void addTvCamera() {
        basket.addProduct tv
        basket.addProduct camera
    }

    // 6.3.4 Verifying number of method calls of the mocked class
    // Listing 6.17 Explicit declaration of interactions
    def 'Warehouse is queried for each product'(){

        when:'user checks out the both products'
        addTvCamera()

        // Mocks are only checks in the when: block.
        boolean readyToShip = basket.canShipCompletely()

        then:'order can be shipped'
        readyToShip // Mocks are only checks in the when: block.
        2 * warehouseInventory.isProductAvailable(_, _) >> true
        0 * warehouseInventory.preload(_, _)

        /* 
         * 
         * 2 * warehouseInventory.isProductAvailable(_, _) >> true
         * 
         * This line says to Spock, “After this test is finished, I expect that the method
         * isProductAvailable() was called twice. I don’t care about the arguments. But when
         * it’s called, please return true to the class that called it.”
         * 
         */
    }

    boolean addTvCameraAndCheckShippingStatus() {
        addTvCamera()
        return basket.canShipCompletely()
    }

    // 6.3.5 Verifying noninteractions for multiple mocked classes
    // Listing 6.18 Verifying interactions for all methods of a class
    def 'Warehouse is queried for each product - strict'() {

        when:'user checks out both products'
        boolean readyToShip = addTvCameraAndCheckShippingStatus()

        then:'order can be shipped'
        readyToShip
        2 * warehouseInventory.isProductAvailable(_, _)  >> true
        1 * warehouseInventory.isEmpty() >> false
        0 * warehouseInventory._

        /**
         * 
         * 0 * inventory._
         * 
         * This line means, “I expect zero invocations for all other methods of the inventory
         * class.” Be careful when using this technique because it means that you know exactly
         * the interface between the class under test and the mock. If a new method is added in
         * the mock (in the production code) that’s used by the class under test, this Spock test
         * will instantly fail.
         * 
         */
    }

    // Listing 6.19 Verifying noninteractions for all mocks
    def 'Only warehouse is queried when checking shipping status' () {

        when:'user checks out both products'
        boolean readyToShip = addTvCameraAndCheckShippingStatus()

        then:'order can be shipped'
        readyToShip
        2 * warehouseInventory.isProductAvailable(_, _)  >> true
        // Underscore matches number of invocations.
        _ * warehouseInventory.isEmpty() >> false
        0 * _  // Underscore matches mocked classes.

        /*
         * In this code listing, the basket class is injected with two mocks (one for shipping costs
         * and one for the inventory). After running the test, you want to verify that only two specific methods were called on the inventory and that nothing was called for the shipping cost service. Instead of manually declaring all other methods with zero
         * cardinality one by one, you use the underscore character in the class part of the verification. In Spock, the line
         * 0 * _
         * means, “I expect zero invocations for all other methods of all other classes when the
         * test runs.” Also notice that you don’t care how many times the isEmpty() method is
         * called, and you use the underscore operator in the cardinality:
         * _ * inventory.isEmpty() >> false
         * This line means, “I expect the isEmpty() method to be called any number of times,
         * and when it does, it should return false.
         */
    }

    // 6.3.6 Verifying types of arguments when a mocked method is called
    // Listing 6.20 Verifying that arguments aren’t null when a mocked method is called
    def 'Warehouse is queried for each product - null'() {

        when:'user checks out both products'
        boolean readyToShip = addTvCameraAndCheckShippingStatus()

        then:'order can be shipped'
        readyToShip
        // Verifying that the first argument isn’t null
        2 * warehouseInventory.isProductAvailable(!null, 1) >> true

        /**
         * 2 * inventory.isProductAvailable(!null ,1) >> true
         * 
         * This line means, “I expect that the method isProductAvailable() will be called 
         * twice. The first argument can be anything apart from null, and the second argument 
         * will always be 1. When that happens, the method will return true.”
         * 
         */
    }

    // Listing 6.21 Verifying the type of arguments
    def 'Warehouse is queried for each product - type '(){
        when:'user checks out both products'
        boolean readyToShip = addTvCameraAndCheckShippingStatus()

        then:'order can be shipped'
        readyToShip
        // Verifying that the first argument is always a string and the second always an integer
        2 * warehouseInventory.isProductAvailable(_ as String, _ as Integer) >> true

        /**
         * here the magic of underscore character is, this time combined with the as keyword. 
         * Notice that a null argument will also fail the verification so the as/underscore 
         * combination includes the null check.
         */
    }
    // Listing 6.22 Verifying exact arguments of a mocked method
    def 'vip status is correctly passed to credit card - simple'(){

        when:'user checks out both products'
        addTvCamera()
        basket.checkout(customer)

        then:'than credit card is chared '
        // Verifying that the second argument is equal to a specific object instance
        1 * creditCardProcessor.sale(1550, customer)

        /*
         * 1 * creditCardSevice.sale(1550, customer)
         * 
         * This line means, “When the test ends, I expect the sale() method to be called exactly
         * once. Its first argument should be the number 1500, and its second argument should
         * be the customer instance.”
         */
    }

    // Listing 6.23 Verifying part of an object instance used as a mock argument
    def 'vip status is correctly passed to credit card - vip'(){

        when:'user checks out both products'
        addTvCamera()
        basket.checkout(customer)

        then:'than credit card is chared '
        1 * creditCardProcessor.sale(1550, {client ->  !client.vip})
    }

    // Listing 6.24 Using full Groovy closures for argument verification
    def 'vip status is correctly passed to credit card - full'() {
        when:'user checks out both products'
        addTvCamera()
        basket.checkout(customer)

        then:'credit card is charged'
        1 * creditCardProcessor.sale({ amount -> amount == basket.findOrderPrice() }, { client -> !client.vip })

        /**
         * This listing uses two closures, one for each argument of the sale() method. As
         * before, the second closure checks a single field of an object (the vip field from the
         * customer class). The first closure makes its own calculation with a completely external
         * method, the findOrderPrice():
         * 
         * 1 * creditCardSevice.sale({amount -> amount ==
         * basket.findOrderPrice()}, { client -> client.vip == false})
         * 
         * The whole line means, “When this unit test is complete, I expect the sale method to
         * be called exactly once. It should have two arguments. The first argument should be
         * equal to the result of basket.findOrderPrice(). The second argument should be an
         * object instance with a vip field. The value of the vip field should be false.”
         * If any facts of this sentence don’t stand, the Spock test will fail. All of them must be
         * correct for a successful test.
         */
    }
}
