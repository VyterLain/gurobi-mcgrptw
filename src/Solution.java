import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Solution {
    private int c;
    private final List<Vehicle> vs = new ArrayList<>();
    private final String status;

    public Solution(String status) {
        this.status = status;
    }

    public Solution(Vehicle[] vs) {
        for (Vehicle i : vs) add(i);
        status = "feasible";
    }

    private void add(Vehicle v) {
        c += v.getC();
        vs.add(v);
    }

    public String solution_type() {
        if (status.equals("feasible")) return "feasible";
        else if (status.equals("not found")) return "not found";
        else return "infeasible";
    }

    public boolean check_solution(Data data) {
        HashSet<Task> set = new HashSet<>(Arrays.asList(data.allTasks.clone()));
        for (Vehicle v : vs) {
            int load = 0;
            for (Task t : v.r) {
                if (t == data.depot) continue;
                load += t.demand;
                if (t.type == Type.Edge) {
                    if (!set.contains(t) && !set.contains(data.getReverseTask(t))) return false;
                } else if (!set.contains(t)) return false;
                set.remove(t);
                set.remove(data.getReverseTask(t));
            }
            if (load > data.capacity) return false;
            if (!(v.r.get(0) == data.depot && v.r.get(v.r.size() - 1) == data.depot)) return false;
        }
        return set.isEmpty();
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
