package sam.learn.spock.where.block

import static org.junit.Assert.*

import org.junit.Test

import com.manning.spock.chapter5.intro.ImageNameValidator

import spock.lang.Specification
import spock.lang.Unroll

class UsingDataPipesSpec extends Specification {

    // Using data pipes for calculating input/output parameters
    // Listing 5.13 Trivial example of data pipes
    def 'Valid images are PNG and JPG'(){
        given:'an image extension checker'
        ImageNameValidator validator = new ImageNameValidator()
        
        expect:'that only valid filenames are allowed'
        validator.isValidImageExtension(fileName) == isValid
        
        where:'sample data are'
        fileName << ["scenery.jpg","house.jpeg", "car.png ","sky.tiff" ,"dance_bunny.gif" ]
        isValid  << [ true, true, false, false, false]
    }
    
    // Listing 5.14 Using Groovy ranges as data generators
    // @Unroll
    def '#a is greater than #b by 3'() {
        expect:'a is equal to b+3' 
        a == (b + 3)
        
        where:'sample data are'
        a << (4..103)
        b << (1..100)
    }

    // Listing 5.15 Using Groovy combinations    
   // @Unroll('Checking image with filename: #fileName ')
    def 'All kinds of JPG files are accepted'() {
        given:'an Image filename validator'
        ImageNameValidator validator = new ImageNameValidator()
        
        expect:'that all jpeg filenames are accepted regardless of case'
        validator.isValidImageExtension(fileName)
        
        where: 'sample image filenames are'
        fileName << GroovyCollections.combinations([
            ['sample.', 'SAMPLE.', 'Sample.'],
            ['j', 'J'],
            ['p', 'P'],
            ['e', 'E', ''],
            ['g', 'G']
            ])*.join()
    }
    
    // Listing 5.16 Constant parameters in Spock tests
    def 'multiplying #first and #second is always nagative number'(){
        expect:'that multiplying a positive number to a negative number is always a negetive value'
        first * second < 0
        
        where:'sample data are'
        first << (1..10)
        second = -1
    } 
    
    // Listing 5.18 Using existing data generators
    def 'Valid images are PNG and JPEG Files '(){
        given:'an Image filename validator'
        ImageNameValidator validator = new ImageNameValidator()
        
        expect:'that all jpeg filenames are accepted regardless of case'
        validator.isValidImageExtension(fileName)
        
        where: 'sample image filenames are loaded from a file validImageNames.txt'
        fileName << new File('validImageNames.txt').readLines()
    }
}
