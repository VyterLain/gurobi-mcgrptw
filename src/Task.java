import java.util.Objects;

public class Task {
    public String name;
    public Type type;
    public int demand;
    public int from;
    public int to;
    public int t_cost;
    public int s_cost;
    public int t_time;
    public int s_time;
    public int begin;
    public int end;

    public Task(String name, Type type, int demand, int from, int to,
                int t_cost, int s_cost, int t_time, int s_time, int begin, int end) {
        this.name = name;
        this.type = type;
        this.demand = demand;
        this.from = from;
        this.to = to;
        this.t_cost = t_cost;
        this.t_time = t_time;
        this.s_cost = s_cost;
        this.s_time = s_time;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return name + '<' + from + ',' + to + '>';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return from == task.from &&
                to == task.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
