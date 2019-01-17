import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static spock.util.matcher.HamcrestSupport.*

import spock.lang.Specification

class HamcrestMatchersSpec extends Specification {
    
        // Listing 4.28 Spock support for Hamcrest matchers

    def 'A exmaple: how to use hamcrest with spock'(){
        given:'list of product names'
        def products = ['camera', 'laptop', 'hifi']
        
        expect:'camera should be one of items'
        products hasItem('camera')
        
        and: 'products does\'nt have hotdog'
        products not(hasItem('hotdog'))
    }
    
    // Listing 4.29 Alternative Spock support for Hamcrest matchers
    def 'A exmaple: how to use hamcrest with spock (2)'(){
        given:'an empty list of product names'
        def products = []
        
        when:'it is filled with products'
        products << 'camera'
        products << 'laptop'
        products << 'hifi'
        
        then:'camera should be one of items'
        expect(products, hasItem('camera'))  // expect() is useful for then: blocks
        
        and: 'products does\'nt have hotdog'
        that(products, not(hasItem('hotdog'))) // that() is useful for and: and expect: Spock blocks.
        
        //  “expect products has item (named) camera, and that products (does) not have item (named) hotdog 
    }
}
