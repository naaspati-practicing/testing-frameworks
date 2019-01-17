package sam.learn.spock.structure;

import sam.learn.spock.Basket;

public class EnterprisyBasket extends Basket {

    private WarehouseInventory warehouseInventory = null;

    public void enableAutoRefresh() {

    }

    public void setNumberOfCaches(int number) {

    }

    public void setCustomerResolver(DefaultCustomerResolver defaultCustomerResolver) {

    }

    public void setWarehouseInventory(WarehouseInventory warehouseInventory) {
        this.warehouseInventory = warehouseInventory;
    }

    public void setLanguage(String language) {

    }

    public void checkout() {
        contents.forEach((p, c) -> warehouseInventory.subtract(p.getName(), c.get()));
    }

}
