import static org.junit.Assert.*

import org.junit.Test

import sam.learn.spock.Basket
import sam.learn.spock.Product
import spock.lang.Specification

class SetupCleanupLifecycleSpec extends Specification {
    Basket basket
    Product tv,camera,hifi

    def setup() {
        tv = new Product(name:'bravia', price:1200, weight:18)
        camera = new Product(name:'Panasonic', price:350, weight:2)
        hifi = new Product(name:'jvc', price:600, weight:5)
        basket = new Basket()
        println 'setup'
    }

    def "A basket with one product weights as that product"() {
        when: "user wants to buy the TV"
        basket.addProduct tv
        
        then: "basket weight is equal to all product weight"
        basket.currentWeight == tv.weight
        
        println 'test1' 
    }
    def "A basket with two products weights as their sum"() {
        when: "user wants to buy the TV and the camera"
        basket.addProduct tv
        basket.addProduct camera
        
        then: "basket weight is equal to all product weight"
        basket.currentWeight == (tv.weight + camera.weight)
        
        println 'test2'
    }
    
    def cleanup() {
        basket.clearAllProducts()
        println 'cleanup\n'
    }
}
