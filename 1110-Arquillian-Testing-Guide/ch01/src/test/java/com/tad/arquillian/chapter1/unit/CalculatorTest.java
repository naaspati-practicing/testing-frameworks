package com.tad.arquillian.chapter1.unit;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tad.arquillian.chapter1.CalculatorController;
import com.tad.arquillian.chapter1.CalculatorData;
import com.tad.arquillian.chapter1.CalculatorForm;
import com.tad.arquillian.chapter1.CalculatorService;
import com.tad.arquillian.chapter1.CalculatorServiceImpl;

@RunWith(Arquillian.class)
public class CalculatorTest {
	
	@Deployment
	public static JavaArchive create() {
		JavaArchive j = ShrinkWrap.create(JavaArchive.class, "test.jar")
				.addPackage(CalculatorData.class.getPackage())
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		
		System.out.println(j.toString(true));
		
		return j;
	}

	@Inject
    CalculatorForm form;
    @Inject
    CalculatorController controller;

    @Test
    public void testInjectedCalculator() {
        form.setX(1);
        form.setY(3);
        form.setZ(5);
        controller.sum();
        Assert.assertEquals(9, form.getSum());
    }

    @Test
    public void testCalculationOfBusinessData() {
        CalculatorData cd = new CalculatorData(1, 3, 5);
        CalculatorService ms = new CalculatorServiceImpl();
        ms.calculateSum(cd);
        Assert.assertEquals(1 + 3 + 5, cd.getCalculatedResult());
    }


}
