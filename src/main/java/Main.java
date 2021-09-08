import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

public class Main {
    static HashMap<Integer, JSONObject> events = new HashMap<>();
    static HashMap<Integer, Order> orders = new HashMap<>();
    static HashMap<Integer, HashMap<Integer, Integer>> result = new HashMap<>();

    public static void main(String[] args) {

        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader("input.txt")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            jsonArray.forEach(user -> parseUserObject((JSONObject) user));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        for (HashMap.Entry<Integer, JSONObject> entry : events.entrySet()) {
            JSONObject obj = entry.getValue();
            int orderID = (int) (long) obj.get("order_id");
            int itemID = (int) (long) obj.get("item_id");
            int count = (int) (long) obj.get("count") - (int) (long) obj.get("return_count");
            boolean shouldBeSent = obj.get("status").equals("OK");
            Order order;
            if (!orders.containsKey(orderID)) {
                order = new Order(itemID, count, shouldBeSent);
            } else {
                order = new Order(itemID, count, shouldBeSent, orders.get(orderID));
            }
            orders.put(orderID, order);
        }

        for (HashMap.Entry<Integer, Order> entry : orders.entrySet()) {
            HashMap<Integer, Integer> temp = new HashMap<>();
            
            for (HashMap.Entry<Integer, Integer> entry2 : entry.getValue().items.entrySet()) {
                if (entry.getValue().shouldBeSent.get(entry2.getKey()) && entry2.getValue() > 0) {
                    temp.put(entry2.getKey(), entry2.getValue());
                }
            }
            if (!temp.isEmpty()) {
                result.put(entry.getKey(), temp);
            }
        }
        JSONArray array = new JSONArray();
        for (HashMap.Entry<Integer, HashMap<Integer, Integer>> entry : result.entrySet()) {
            JSONObject js = new JSONObject();
            JSONArray temp = new JSONArray();
            for (HashMap.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
                JSONObject t = new JSONObject();
                t.put("count", entry2.getValue());
                t.put("id", entry2.getKey());
                temp.add(t);
            }
            js.put("id", entry.getKey());
            js.put("items", temp);
            array.add(js);
        }
        System.out.println(array);
    }

    private static void parseUserObject(JSONObject user) {
        int event_id = (int) (long) user.get("event_id");
        events.put(event_id, user);
    }
}


