import java.util.HashMap;

public class Order {
    HashMap<Integer, Boolean> shouldBeSent = new HashMap<>();
    HashMap<Integer, Integer> items = new HashMap<>();

    public Order(int itemID, int count, boolean shouldBeSent) {
        items.put(itemID, count);
        this.shouldBeSent.put(itemID, shouldBeSent);
    }

    public Order(int itemID, int count, boolean shouldBeSent, Order order) {
        this.items.putAll(order.items);
        this.shouldBeSent.putAll(order.shouldBeSent);
        this.items.put(itemID, count);
        this.shouldBeSent.put(itemID, shouldBeSent);
    }
}