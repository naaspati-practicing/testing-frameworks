package annotations

import java.util.concurrent.TimeUnit

import com.manning.spock.chapter8.eshop.BillableBasket
import com.manning.spock.chapter8.eshop.CreditCardProcessor
import com.manning.spock.chapter8.eshop.Product
import com.manning.spock.chapter8.loan.Customer

import spock.lang.Specification
import spock.lang.Timeout

class TimeoutSpec extends Specification {
    // Listing 8.7 Declaring a test time-out

    @Timeout(5)
    def "credit card charge happy path"() {
        given: "a basket, a customer and a TV"
        Product tv = new Product(name:"bravia",price:1200,weight:18)
        BillableBasket basket = new BillableBasket()
        Customer customer = new
                Customer(name:"John",vip:false,creditCard:"testCard")
        and: "a credit card service"
        CreditCardProcessor creditCardSevice = new CreditCardProcessor()
        basket.setCreditCardProcessor(creditCardSevice)
        when: "user checks out the tv"
        basket.addProduct tv
        boolean success = basket.checkout(customer)
        then: "credit card is charged"
        success
    }

    // Listing 8.8 Declaring a test time-out—custom unit
    @Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
    def "credit card charge happy path - alt "() {
        given: "a basket, a customer and a TV"
        Product tv = new Product(name:"bravia",price:1200,weight:18)
        BillableBasket basket = new BillableBasket()
        Customer customer = new
                Customer(name:"John",vip:false,creditCard:"testCard")
        and: "a credit card service"
        CreditCardProcessor creditCardSevice = new CreditCardProcessor()
        basket.setCreditCardProcessor(creditCardSevice)
        when: "user checks out the tv"
        basket.addProduct tv
        boolean success = basket.checkout(customer)
        then: "credit card is charged"
        success
    }
}
