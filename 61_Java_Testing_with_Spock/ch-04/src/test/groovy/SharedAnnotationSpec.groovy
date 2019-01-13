

import static org.junit.Assert.*

import org.junit.Test

import sam.learn.spock.Basket
import sam.learn.spock.Product
import sam.learn.spock.lifecycle.BillableBasket
import sam.learn.spock.lifecycle.CreditCardProcessor
import spock.lang.Shared
import spock.lang.Specification

class SharedAnnotationSpec extends Specification {

    @Shared
    CreditCardProcessor processor;  //will be created only once

    def setupSpec() {
        processor = new CreditCardProcessor()
    }
    def cleanupSpec() {
        processor.shutDown()
    }

    BillableBasket basket

    def setup() {
        basket = new BillableBasket()
    }
    def cleanup() {
        basket.clearAllProducts()
    }
    def "user buys a single product"() {
        given: "an empty basket and a TV"
        Product tv = new Product(name:"bravia",price:1200,weight:18)

        and: "user wants to buy the TV"
        basket.addProduct tv

        when: "user checks out"
        basket.checkout()

        then: "revenue is the same as the price of TV"
        processor.currentRevenue == tv.price
    }
    def "user buys two products"() {
        given: "an empty basket and a camera"
        Product camera = new Product(name:"panasonic",price:350,weight:2)

        and: "user wants to two cameras"
        basket.addProduct(camera,2)

        when: "user checks out"
        basket.checkout()

        then: "revenue is the same as the price of both products"
        processor.currentRevenue == 2 * camera.price
    }
}
