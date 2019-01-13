package com.manning.spock.chapter5.intro

import static org.junit.Assert.*

import org.junit.Test

import spock.lang.Specification

class ImageNameValidatorSpec extends Specification {

    // Listing 5.2 Simple Spock parameterized test
    def 'valid images are PNG and JPG'(){
        given: 'an image extension checker'
        ImageNameValidator validator = new ImageNameValidator()

        expect:'that only valid filenames are accepted'
        validator.isValidImageExtension(imgFileName) == isValid

        where:'sample image names are'
        imgFileName       || isValid
        'scenery.jpg'     || true
        'house.jpeg'      || true
        'car.png'         || true
        'sky.tiff'        || false
        'dance_bunny.gif' || false
    }

    // Listing 5.3 The given-when-then-where structure
    def 'Valid images are PNG and JPEG files (enterprise version)'() {
        given: 'an image extension checker'
        ImageNameValidator validator = new ImageNameValidator()
        
        when: 'an image is checked'
        ImageExtensionCheck imageExtensionCheck =
        validator.examineImageExtension(pictureFile)
        
        then: 'expect that only valid filenames are accepted'
        imageExtensionCheck.with { 
            result == isValid
            errorCode == error
            errorDescription == description
        }
        
        where: 'sample image names are'
        pictureFile       || isValid      | error      | description
        'scenery.jpg'     || true         | ''         | ''
        'house.jpeg'      || true         | ''         | ''
        'car.png'         || true         | ''         | ''
        'sky.tiff'        || false        | 'ERROR002' | 'Tiff files are not supported'
        'dance_bunny.gif' || false        | 'ERROR999' | 'Unsupported file type'
        }

}
