package sam.learn.spock.lifecycle;

import sam.learn.spock.Basket;

public class BillableBasket extends Basket {
    private CreditCardProcessor creditCardProcessor;

    public BillableBasket() {
        System.out.println("BillableBasket is starting");
    }

    public void checkout() {
        if (creditCardProcessor == null) {
            return;
        }
        int[] sum = {0};
        contents.forEach((product, count) -> {
            sum[0] += product.getPrice() * count.get();
        });
        creditCardProcessor.charge(sum[0]);
    }

    public void setCreditCardProcessor(CreditCardProcessor creditCardProcessor) {
        this.creditCardProcessor = creditCardProcessor;
    }


}
