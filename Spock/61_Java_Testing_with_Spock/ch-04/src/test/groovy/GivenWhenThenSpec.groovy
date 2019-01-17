import static org.junit.Assert.*

import sam.learn.spock.Basket
import sam.learn.spock.Product
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Narrative(/ A empty basket starts with no
weight. Adding products to the basket
increases its weight. The weight is
then used for billing during shipping calculations.
Electronic goods have always zero weight.
/)
@Title("Unit test for basket weight")
@Subject(Basket)
class GivenWhenThenSpec extends Specification {

    def 'A basket with one product has same weight as the product'(){
        given: 'an empty basket with a TV'
        Product product = new Product(name:'bravia', price:1200, weight:18)
        Basket basket = new Basket()

        when: 'user wants to buy tv'
        basket.addProduct(product)

        then:'basket weight is equal to product weight'
        basket.currentWeight == product.weight
    }

    // using setup: block
    def 'A basket with one product has same weight as the product - (2)'(){
        setup: 'an empty basket with a TV'
        Product product = new Product(name:'bravia', price:1200, weight:18)
        Basket basket = new Basket()

        when: 'user wants to buy tv'
        basket.addProduct(product)

        then:'basket weight is equal to product weight'
        basket.currentWeight == product.weight
    }

    // using and: block, as extension of when: black
    def 'A basket with three products weighs same as sum of those three product\'s weights'(){
        given:'An empty basket'
        Basket basket = new Basket()

        and:'Three products'
        Product tv = new Product(name:'bravia', price:1200, weight:18)
        Product camera = new Product(name:'Panasonic', price:350, weight:2)
        Product hifi = new Product(name:'jvc', price:600, weight:5)

        when:'user wants to buy TV, camera and the hifi'
        basket.addProduct tv
        basket.addProduct camera
        basket.addProduct hifi

        then:'weight of basket  is equal to sum of weights of products'
        basket.currentWeight == [tv, camera, hifi]*.weight.sum()
    }

    // using and: block, as extension of when: black and  then block
    def 'if three products is added to basket, then basket must have 3 products and weight of  basket is sum of weights of products '(){
        given:'An empty basket'
        Basket basket = new Basket()

        and:'Three products'
        Product tv = new Product(name:'bravia', price:1200, weight:18)
        Product camera = new Product(name:'Panasonic', price:350, weight:2)
        Product hifi = new Product(name:'jvc', price:600, weight:5)

        when:'user wants to buy TV..'
        basket.addProduct tv

        and: '..the camera..'
        basket.addProduct camera

        and:'..the hifi'
        basket.addProduct hifi

        then:'weight of basket  is equal to sum of weights of products'
        basket.currentWeight == [tv, camera, hifi]*.weight.sum()

        and:'basket contains three products'
        basket.getProductTypesCount() == 3
    }

    // Listing 4.11 Trivial tests with the expect: block
    def 'An empty basket has no weight'() {
        expect: 'zero weight when nothing is in the basket'
        new Basket().currentWeight == 0
    }

    // Listing 4.12 expect: block replaces when: and then:
    def 'An empty basket has no weight (2)'() {
        given: 'an empty basket'
        Basket basket = new Basket()

        expect:'the weight is 0'
        basket.currentWeight == 0 // expect: block performs the assertion of the test
    }

    // Listing 4.13 Using expect: for preconditions
    def "A basket with two products weights as their sum (precondition)"() {
        given:'An empty basket'
        Basket basket = new Basket()

        and:'Three products'
        Product tv = new Product(name:'bravia', price:1200, weight:18)
        Product camera = new Product(name:'Panasonic', price:350, weight:2)
        Product hifi = new Product(name:'jvc', price:600, weight:5)

        expect: 'basket should be empty'
        basket.currentWeight == 0
        basket.productTypesCount == 0

        when:'user wants to buy TV, camera and the hifi'
        basket.addProduct tv
        basket.addProduct camera
        basket.addProduct hifi

        then:'weight of basket  is equal to sum of weights of products'
        basket.currentWeight == [tv, camera, hifi]*.weight.sum()
    }

    // Listing 4.14 Using cleanup: to release resources even if test fails
    def 'A basket with one product has equal weight'(){
        given: 'an empty basket with a TV'
        Product product = new Product(name:'bravia', price:1200, weight:18)
        Basket basket = new Basket()

        when: 'user wants to buy tv'
        basket.addProduct(product)

        then:'basket weight is equal to product weight'
        basket.currentWeight == product.weight

        cleanup: 'empty the basket'
        basket.clearAllProducts()
    }
    
    // Listing 4.16 Marking the class under test
    def "A basket with two products weights as their sum (better)"() {
        given: "an empty basket"
        @Subject
        Basket basket = new Basket()
        
        and: "several products"
        Product tv = new Product(name:"bravia",price:1200,weight:18)
        Product camera = new Product(name:"panasonic",price:350,weight:2)
        
        when: "user wants to buy the TV and the camera and the hifi"
        basket.addProduct tv
        basket.addProduct camera
        
        then: "basket weight is equal to all product weight"
        basket.currentWeight == (tv.weight + camera.weight)
    }
}
