package sam.learn.spock;

import java.util.HashMap;
import java.util.Map;

public class Basket {
    protected Map<Product, Count> contents = new HashMap<>();
    
    public void clearAllProducts() {
        contents.clear();
    }
    public void addProduct(Product product) {
        addProduct(product, 1);
    }
    public void addProduct(Product product, int count) {
        contents.putIfAbsent(product, new Count());
        Count c = contents.get(product);
        if(c == null) {
            c = new Count();
            contents.put(product, c);
        }
        c.increaseBy(count);
    }
    public int getCurrentWeight() {
        return contents.keySet().stream().mapToInt(Product::getWeight).sum();
    }
    public int getProductTypesCount() {
        return contents.size();
    }

}
