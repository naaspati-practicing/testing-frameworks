import static org.junit.Assert.*

import sam.learn.spock.Basket
import sam.learn.spock.Product

class MultipleWhenThenSpec {

    // Listing 4.24 Multiple when-then blocks
    def "Adding products to a basket increases its weight"() {
        given: "an empty basket"
        Basket basket = new Basket()

        and: "a two products"
        Product tv = new Product(name:"bravia",price:1200,weight:18)
        Product camera = new Product(name:"panasonic",price:350,weight:2)

        when: "user gets the camera"
        basket.addProduct(camera)

        then: "basket weight is updated accordingly"
        basket.currentWeight == camera.weight

        when: "user gets the tv too"
        basket.addProduct(tv)

        then: "basket weight is updated accordingly"
        basket.currentWeight == camera.weight + tv.weight
    }
}


/*
 * This pattern must be used with care. It can be used correctly as a way to test a
 * sequence of events (as demonstrated in this listing). If used incorrectly, it might also
 * mean that your test is testing two things and should be broken.
 * Use common sense when you structure Spock tests. If writing descriptions next to
 * Spock blocks is becoming harder and harder, it might mean that your test is doing
 * complex things.
 * 
 * */
