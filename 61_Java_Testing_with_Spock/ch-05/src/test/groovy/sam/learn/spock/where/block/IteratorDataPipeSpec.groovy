package sam.learn.spock.where.block

import static org.junit.Assert.*

import com.manning.spock.chapter5.intro.ImageNameValidator

import spock.lang.Specification

class IteratorDataPipeSpec extends Specification {

    // Listing 5.20 Using Java iterators in Spock
    def 'valid images are PNG and and JPEG'(){
        given:'an image extension checker'
        ImageNameValidator validator = new ImageNameValidator()

        expect:'that only valid filenames are allowed'
        !validator.isValidImageExtension(fileName)

        where:'sample data are'
        fileName << getInvalidNameIterator()
    }

    def getInvalidNameIterator() {
        def values = [
            'starsystem.tiff',
            'galaxy.tif',
            'bunny04.gif',
            'looper.GIF',
            'dolly_take.mov',
            'afar.xpm'
        ]
        int index = 0;

        return [
            hasNext:{index < values.size()},
            next:{values[index++]}
        ] as Iterator
    }

    // Listing 5.22 Using multivalued iterators in Spock
    def 'valid images are PNG and and JPEG (2)'(){
        given:'an image extension checker'
        ImageNameValidator validator = new ImageNameValidator()

        expect:'that only valid filenames are allowed'
        validator.isValidImageExtension(fileName) == isValid

        where:'sample data are'
        [fileName, isValid]<< getMultiValuedIterator()

    }
    def getMultiValuedIterator() {
        def names =  ["scenery.jpg", "house.jpeg", "car.png ", "sky.tiff" , "dance_bunny.gif"]
        def results = [true, true, false, false, false]

        int index = 0;

        return [
            hasNext:{ index < names.size() },
            next:{
                [names[index], results[index++]]}
        ] as Iterator
    }
    // Listing 5.23 Using multivalued assignments in Spock
    // @Unroll("Checking harcoded image name #pictureFile with #result")
    def "Valid images are PNG and JPEG files only"() {
        given: "an image extension checker"
        ImageNameValidator validator = new ImageNameValidator()
        expect: "that all filenames are categorized correctly"
        validator.isValidImageExtension(pictureFile) == result
        where: "sample image names are"
        [pictureFile, result]<< [["sample.jpg", true], ["bunny.gif", false]]
    }
}
