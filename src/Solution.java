import java.util.ArrayList;
import java.util.List;

public class Solution {
    private int c;
    private final List<Vehicle> vs = new ArrayList<>();

    public Solution(Data data, Vehicle[] vs) {
        for (Vehicle i : vs) add(i);
    }

    private void add(Vehicle v) {
        c += v.getC();
        vs.add(v);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Solution{cost => ");
        sb.append(c).append(", routes =>\n");
        for (Vehicle r : vs) sb.append('\t').append(r.toString()).append('\n');
        sb.append('}');
        return sb.toString();
    }
}
