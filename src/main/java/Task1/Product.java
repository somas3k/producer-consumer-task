package Task1;

public class Product {
    private int condition; // condition == 0 means that producer let a new production

    public Product(int condition) {
        this.condition = condition;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
