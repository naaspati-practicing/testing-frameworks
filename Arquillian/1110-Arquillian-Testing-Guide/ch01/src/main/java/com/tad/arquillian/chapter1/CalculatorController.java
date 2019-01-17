package com.tad.arquillian.chapter1;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class CalculatorController {
	@PostConstruct
	public void init() {
		Logger.getGlobal().info("created: "+getClass()+"  "+hashCode());
	}

    @Inject
    private CalculatorService service;
    @Inject
    private CalculatorForm    form;
    
    public void sum() {
        CalculatorData data = new CalculatorData(form.getX(), form.getY(),
                form.getZ());
        service.calculateSum(data);
        form.setSum(data.getCalculatedResult());
    }
}
