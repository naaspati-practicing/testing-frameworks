package sam.learn.spock;

public class Count {
    private int count = 0;
    
    public Count() {}
    public Count(int count) {
        this.count = count;
    }
    
    public void increment() {
        count++;
    }
    public void decrement() {
        count--;
    }
    public int get() {
        return count;
    }
    public void set(int count) {
        this.count = count;
    }
    public void increaseBy(int add) {
        count += add;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + count;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Count other = (Count) obj;
        if (count != other.count)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Count [count=" + count + "]";
    }
    
}
