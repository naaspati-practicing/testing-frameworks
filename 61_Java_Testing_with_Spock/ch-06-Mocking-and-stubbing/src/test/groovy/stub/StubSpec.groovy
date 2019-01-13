package stub
import static org.junit.Assert.*

import com.manning.spock.chapter6.Basket
import com.manning.spock.chapter6.Product
import com.manning.spock.chapter6.stubs.WarehouseInventory

import spock.lang.Narrative
import spock.lang.Specification

class StubSpec extends Specification {
    Product tv
    WarehouseInventory inventory
    Basket basket
    Product camera
    
    def setup() {
        given:'a basket and a tv and a camera'
        tv = new Product(name:"bravia",price:1200,weight:18)
        camera = new Product(name:"panasonic",price:350,weight:2)
        basket = new Basket()
        
        and:'an empty warehouse'
        inventory = Stub(WarehouseInventory)  // create spock stub
        basket.warehouseInventory = inventory
    }
    
    void addProducts() {
        basket.with{
            addProduct tv
            addProduct camera
        }
    }
    
    // Listing 6.2 Creating a simple stub with Spock
    def 'if warehouse is empty nothing can be shipped'(){
        given:'when basket checks inventory status '
        inventory.isEmpty() >> true // Instructs the stub to return true when isEmpty() is called
        
        when:'user checks out tv'
        basket.addProduct tv
        
        then:'order cannot be shipped'
        !basket.canShipCompletely()
    }
    
    // Listing 6.3 Stubbing specific arguments
    def 'if warehouse has the product in stock everything is fine'(){
        given:'warehouse is not empty and has stock for product for tv'
        inventory.isProductAvailable(tv.name, 1) >> true // Instructing the stub to return true when specific arguments are used
        inventory.isEmpty() >> false
        
        when:'user checks out tv'
        basket.addProduct tv
        
        then:'order can be shipped right away'
        basket.canShipCompletely()
    }
    
 // Listing 6.4 Argument-based stub differentiation
    def "If warehouse does not have all products, order cannot be shipped"(){
        given:'warehouse does not have some products in stock'
        // Different stub results depending on the argument
        inventory.isProductAvailable(tv.name, 1) >> true
        inventory.isProductAvailable(camera.name, 1) >> false
        inventory.isEmpty() >> false
        
        when: 'user checks out both products'
        addProducts()
        
        then:'order cannot be shiiped right away'
        !basket.canShipCompletely()
    }
    
    // Listing 6.5 Grouping all stubbed methods
    def "If warehouse does not have all products, order cannot be shipped (2)"(){
        given:'warehouse does not have some products in stock'
        WarehouseInventory inventory2 = Stub(WarehouseInventory) {
            isProductAvailable(tv.name, 1) >> true
            isProductAvailable(camera.name, 1) >> false
            isEmpty() >> false
        }
        basket.warehouseInventory = inventory2
        
        when: 'user checks out both products'
        addProducts()
        
        then:'order cannot be shiiped right away'
        !basket.canShipCompletely()
    }
    
    // 6.2.2 Matching arguments leniently when a stubbed method is called
    // Listing 6.6 Using argument matchers in stubs
    
    def "If warehouse has both products everything is fine"() {
        given:'a warehouse with enough stock'
        inventory.isProductAvailable(_, 1) >> true // Stubbing a method call regardless of the value of an argument
        
        when:'user checks out both products'
        addProducts()
        
        then:'order can be shipped right away'
        basket.canShipCompletely()
    }
    
    // Listing 6.7 Ignoring all arguments of a stubbed method when returning a response
    def "If warehouse is fully stocked than everything is fine"() {
        given:'a warehouse with full stock'
        inventory.isProductAvailable(_, _) >> true // Stubbing a method for all its possible arguments
        
        when:'user checks out multiple products'
        basket.with { 
            addProduct camera, 33
            addProduct tv, 50 
        }
        
        then:'order can be shipped right away'
        basket.canShipCompletely()
    }
    
    // Listing 6.8 Stubbing subsequent method calls
    def 'Inventory is always checked in the last possible moment'(){
        given:'a warehouse with fluctuating stock levels'
        // First call will return true, and second will return false.
        inventory.isProductAvailable(tv.name, _) >>> true >> false
        inventory.isEmpty() >>> [false, true]  // spock can also iterate on a collection for ordered responses
        
        when:'user checks out the tv'
        basket.addProduct tv
        
        then:'order can be shipped right away'
        basket.canShipCompletely()
        
        when:'user checks out another tv'
        basket.addProduct tv
        
        then:'order cannot be shipped right away'
        !basket.canShipCompletely()
    }
    
    // Listing 6.9 Instructing stubs to throw exceptions
    def 'A problematic inventory means nothing can be shipped'() {
        given:'warehouse has serious issues'
        inventory.isProductAvailable(tv.name, _) >> {throw new RuntimeException('critical error')}
        
        when:'user checks out another tv'
        basket.addProduct tv
        
        then:'order cannot be shipped'
        !basket.canShipCompletely()
    }
}
