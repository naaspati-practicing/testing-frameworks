package sam.learn.spock.where.block

import static org.junit.Assert.*

import com.manning.spock.chapter5.intro.ImageNameValidator

import sam.learn.spock.adder.Adder
import spock.lang.Specification
import spock.lang.Unroll

class UsingDataTablesSpec extends Specification {
    def 'seperating table using single pipe'(){
        given:'an adder'
        Adder adder = new Adder()

        expect: 'that it calculated sum of two numbers'
        adder.add(a, b) == sum

        where:'possible scenerio  are'
        a  | b  | sum
        1  | 1  | 2
        3  | 2  | 5
        82 | 16 | 98
        3  | -3 | 0
        0  | 0  | 0
    }

    def 'simple two variable sum test'(){
        given:'an adder'
        Adder adder = new Adder()

        expect: 'that it calculated sum of two numbers'
        adder.add(a, b) == sum

        where:'possible scenerio  are'
        a  | b  || sum
        1  | 1  || 2
        3  | 2  || 5
        82 | 16 || 98
        3  | -3 || 0
        0  | 0  || 0
    }

    // Listing 5.6 Data tables with one column
    def 'Tiff, gif, raw,mov and bmp are invalid extensions'() {
        given: 'an image extension checker'
        ImageNameValidator validator = new ImageNameValidator()

        expect: 'that only valid filenames are accepted'
        !validator.isValidImageExtension(pictureFile)  // Output parameter is always false for this test. All images are invalid.

        where: 'sample image names are'
        pictureFile       || _
        'screenshot.bmp'  || _
        'IMG3434.raw'     || _
        'christmas.mov'   || _
        'sky.tiff'        || _
        'dance_bunny.gif' || _

        // underscore acts as dummy filler for the Boolean result of the test
    }
    
    @Unroll
    def 'Trivial adder test'() {
        given: 'an adder'
        Adder adder = new Adder()
        
        when: 'the add method is called for two numbers'
        int result = adder.add(first,second)
        
        then: 'the result should be the sum of them'
        result ==sum
        
        where: 'some scenarios are'
        first | second || sum
        1     | 1      || 2
        3     | 2      || 5
        3     | -3     || 0
    }
    // Listing 5.10 Printing parameters of each scenario
    @Unroll('Adder test #first + #second = #sum')
    def 'Trivial adder test (2)'() {
        given: 'an adder'
        Adder adder = new Adder()
        
        when: 'the add method is called for two numbers'
        int result = adder.add(first,second)
        
        then: 'the result should be the sum of them'
        result ==sum
        
        where: 'some scenarios are'
        first | second || sum
        1     | 1      || 2
        3     | 2      || 5
        3     | -3     || 0
    }
    
    // Listing 5.11 Parameter rendering on the test method
    @Unroll // Parameters inside the method name instead of using the unroll string
    def 'Trivial adder test (3): #first + #second = #sum '() {
        given: 'an adder'
        Adder adder = new Adder()
        
        when: 'the add method is called for two numbers'
        int result = adder.add(first,second)
        
        then: 'the result should be the sum of them'
        result ==   sum
        
        where: 'some scenarios are'
        first | second || sum
        1     | 1      || 2
        3     | 2      || 5
        3     | -3     || 0
    }
}
