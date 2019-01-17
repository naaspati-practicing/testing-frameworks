package sam.learn.spock.lifecycle;

public class CreditCardProcessor {
    private int todaySalary;
    
    public CreditCardProcessor() {
        System.out.println("Expensive creadit processor is starting");
    }
    public void newDayStarted() {
        todaySalary = 0;
    }
    public void charge(int price) {
        todaySalary += price;
    }
    public int getCurrentRevenue() {
        return todaySalary;
    }
    public void shutDown() {
        System.out.println("Expensive creadit processor is stopping");
    }
}
