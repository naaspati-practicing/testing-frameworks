package stub
import static org.junit.Assert.*

import com.manning.spock.chapter6.Basket
import com.manning.spock.chapter6.Product
import com.manning.spock.chapter6.stubs.EnterprisyBasket
import com.manning.spock.chapter6.stubs.ServiceLocator
import com.manning.spock.chapter6.stubs.ShippingCalculator
import com.manning.spock.chapter6.stubs.WarehouseInventory

import spock.lang.Narrative
import spock.lang.Specification

class Stub2Spec extends Specification {
    Product tv,laptop,hifi,camera
    WarehouseInventory inventory
    Basket basket
    ShippingCalculator calculator

    def setup() {
        given:'few products'
        tv = new Product(name:"bravia",price:1200,weight:18)
        camera = new Product(name:"panasonic",price:350,weight:2)
        hifi = new Product(name:"jvc",price:600,weight:5)
        laptop = new Product(name:"toshiba",price:800,weight:10)

        and:'a basket'
        basket = new Basket()

        and:'an empty warehouse and'
        inventory = Stub(WarehouseInventory)  // create spock stub
        basket.warehouseInventory = inventory

        and:'a shipping calculator'
        calculator = Stub(ShippingCalculator)
        basket.shippingCalculator = calculator
    }
    void addProducts() {
        basket.with{
            addProduct tv
            addProduct camera
        }
    }

    // Listing 6.10 Stubs that respond according to arguments
    def 'Basket handles shipping charges according to product count'(){
        given:'A fully stocked warehouse'
        inventory.isProductAvailable(_, _) >> true

        and:'a shipping calculator that charges 10 dollars for each product'
        calculator.findShippingCostFor(_, _) >> { product, count -> count * 10 }

        and:'a map describing product and counts'
        def map = [(tv):2, (camera):2, (hifi):1, (laptop):3]

        when:'user checks out the given products in given quantities'
        map.each { product, count ->  basket.addProduct(product, count) }

        then:'cost is correctly calculated'
        basket.findTotalCost() == map.keySet().collect{product -> product.price * map[product]}.sum() + ( basket.getProductCount() * 10 )
    }

    // Listing 6.11 A smart stub that looks at both its arguments
    def 'Downloadable goods do not have shipping cost'(){
        given:'some more prducts'
        Product ebook = new Product(name:"learning exposure",price:30,weight:0)
        Product suite = new Product(name:"adobe essentials",price:200,weight:0)

        and:'A fully stocked warehouse'
        inventory.isProductAvailable(_, _) >> true

        and:'a shipping calculator that charges 10 dollars for each product'
        calculator.findShippingCostFor(_, _) >> { product, count -> product.weight == 0 ? 0 : count * 10 }

        and:'a map describing product and counts'
        def map = [(tv):2, (camera):2, (hifi):1, (laptop):3, (ebook):5, (suite):6]

        when:'user checks out the given products in given quantities'
        map.each { product, count ->  basket.addProduct(product, count) }

        then:'cost is correctly calculated'
        def totalPrice = map.collect{product, count -> product.price * count }.sum()
        def shippingChargeExcludingZeroWeight = map.findAll { product, count -> product.weight != 0 }.values().sum() * 10

        basket.findTotalCost() ==  totalPrice + shippingChargeExcludingZeroWeight
    }
    
    // Listing 6.12 Stubbing responses with other stubs
    // this example show a two level stub, more level can created
    def  'If warehouse is empty nothing can be shipped'(){
        given:'an empty warehouse'
        inventory.isEmpty() >> true
        
        and:'a ServiceLocator'
        ServiceLocator locator = Stub(ServiceLocator)
        locator.getWarehouseInventory() >> inventory // Stubbing of intermediary class
        
        and:'an enterprise basket'
        EnterprisyBasket basket = new EnterprisyBasket(locator)
        
        when:'user checks out the tv'
        basket.addProduct tv
        
        then:'order cannot be shipped'
        !basket.canShipCompletely()
    }

}
