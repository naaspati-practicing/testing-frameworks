package sam.learn.spock.structure

import static org.junit.Assert.*

import sam.learn.spock.Product
import spock.lang.Specification

class WarehouseInventorySpec extends Specification {

    // Listing 4.33 Assertions and setup on the same object
    def 'Buying products reduces the invetory availability'(){
        given:'an inventory with products'
        Product laptop = new Product(name:"toshiba",price:1200,weight:5)
        Product camera = new Product(name:"panasonic",price:350,weight:2)
        Product hifi = new Product(name:"jvc",price:600,weight:5)

        WarehouseInventory warehouseInventory = new WarehouseInventory()
        warehouseInventory.preload(laptop, 3)
        warehouseInventory.preload(camera, 5)
        warehouseInventory.preload(hifi, 2)

        and:'an empty basket'
        EnterprisyBasket basket = new EnterprisyBasket()
        basket.setWarehouseInventory(warehouseInventory)
        basket.setCustomerResolver(new DefaultCustomerResolver())
        basket.setLanguage("english")
        basket.setNumberOfCaches(3)
        basket.enableAutoRefresh()

        when: "user gets a laptop and two cameras"
        basket.addProduct(camera,2)
        basket.addProduct(laptop)

        and: "user completes the transaction"
        basket.checkout()

        then: "warehouse is updated accordingly"
        !warehouseInventory.isEmpty()
        warehouseInventory.getBoxesMovedToday() == 3
        warehouseInventory.availableOfProduct("toshiba") == 2
        warehouseInventory.availableOfProduct("panasonic") == 3
        warehouseInventory.availableOfProduct("jvc") == 2
    }

    // Listing 4.34 Grouping similar code with Groovy and Spock
    def 'Buying products reduces the invetory availability optimized with "with"'(){
        given:'an inventory with products'
        Product laptop = new Product(name:"toshiba",price:1200,weight:5)
        Product camera = new Product(name:"panasonic",price:350,weight:2)
        Product hifi = new Product(name:"jvc",price:600,weight:5)

        WarehouseInventory warehouseInventory = new WarehouseInventory()
        warehouseInventory.with{
            preload laptop, 3
            preload camera, 5
            preload hifi, 2
        }

        and:'an empty basket'
        EnterprisyBasket basket = new EnterprisyBasket()
        basket.with{
            setWarehouseInventory(warehouseInventory)
            customerResolver = new DefaultCustomerResolver()
            language =  'english'
            numberOfCaches = 3
            enableAutoRefresh()
        }

        when: "user gets a laptop and two cameras"
        basket.with{
            addProduct camera,2
            addProduct laptop
        }

        and: "user completes the transaction"
        basket.checkout()

        then: "warehouse is updated accordingly"
        warehouseInventory.with {
            !isEmpty()
            boxesMovedToday == 3
            availableOfProduct("toshiba") == 2
            availableOfProduct("panasonic") == 3
            availableOfProduct("jvc") == 2
        }
    }
}
