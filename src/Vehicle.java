import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private final Data data;
    private final int id;
    private int c;
    public final List<Task> r = new ArrayList<>();

    public Vehicle(Data data, int id, Task[] ts) {
        this.data = data;
        this.id = id;
        r.add(data.depot);
        for (Task i : ts) add(i);
        add(data.depot);
    }

    private void add(Task t) {
        c += t.t_cost;
        c += data.nodeDistGraph[r.get(r.size() - 1).to][t.from];
        r.add(t);
    }

    public int getC() {
        return c;
    }

    @Override
    public String toString() {
        return "Route[" + id + "]-{" +
                "cost => " + c +
                ", route => " + r + '}';
    }
}
