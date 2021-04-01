public class Data {
    public String instName;
    public int optimalValue;
    public int vehicles;
    public int capacity;
    public int depotNode;
    public int nodes;
    public int edges;
    public int arcs;
    public int requiredN;
    public int requiredE;
    public int requiredA;
    public Task[] allTasks; // nodes, arcs, edges, reverseEdges
    public Task[] nodeTasks;
    public Task[] edgeTasks;
    public Task[] edgeReverseTasks;
    public Task[] arcTasks;
    public int[][] nodeDistGraph; // includes depot, index 0 is useless
    public int[][] taskDistGraph; // includes depot, index 0 is depot
    public int[][] nodeTimeGraph;
    public int[][] taskTimeGraph;

    public void initBasicInfo(int value, int type) {
        switch (type) {
            case 1 -> this.optimalValue = value;
            case 2 -> this.vehicles = value;
            case 3 -> this.capacity = value;
            case 4 -> this.depotNode = value;
            case 5 -> this.nodes = value;
            case 6 -> this.edges = value;
            case 7 -> this.arcs = value;
            case 8 -> this.requiredN = value;
            case 9 -> this.requiredE = value;
            case 10 -> this.requiredA = value;
        }
    }

    public void initArray() {
        allTasks = new Task[requiredN + requiredE * 2 + requiredA];
        nodeTasks = new Task[requiredN];
        edgeTasks = new Task[requiredE];
        edgeReverseTasks = new Task[requiredE];
        arcTasks = new Task[requiredA];
        nodeDistGraph = new int[nodes + 1][nodes + 1];
        nodeTimeGraph = new int[nodes + 1][nodes + 1];
        taskDistGraph = new int[allTasks.length + 1][allTasks.length + 1];
        taskTimeGraph = new int[allTasks.length + 1][allTasks.length + 1];
    }

    public void initTasksInfo(String[] values, int count) {
        if (count >= 0 && count < requiredN)
            createNodeTask(values, count);
        else if (count >= requiredN && count < requiredN + requiredE)
            createEdgeTask(values, count - requiredN);
//        else if (count >= requiredN + requiredE && count < requiredN + edges)
//            completeGraph(values);
        else if (count >= requiredN + edges && count < requiredN + edges + requiredA)
            createArcTask(values, count - requiredN - edges);
        else completeGraph(values);
    }

    private void createNodeTask(String[] values, int index) {
        int node = Integer.parseInt(values[0].substring(1));
        int demand = Integer.parseInt(values[1]);
        int s_cost = Integer.parseInt(values[2]);
        int s_time = Integer.parseInt(values[3]);
        int begin = Integer.parseInt(values[4]);
        int end = Integer.parseInt(values[5]);
        Task t = new Task(values[0], Type.Node, demand, node, node, 0, s_cost, 0, s_time, begin, end);
        allTasks[index] = t;
        nodeTasks[index] = t;
    }

    private void createEdgeTask(String[] values, int index) {
        int from = Integer.parseInt(values[1]);
        int to = Integer.parseInt(values[2]);
        int t_cost = Integer.parseInt(values[3]);
        int demand = Integer.parseInt(values[4]);
        int s_cost = Integer.parseInt(values[5]);
        int t_time = Integer.parseInt(values[6]);
        int s_time = Integer.parseInt(values[7]);
        int begin = Integer.parseInt(values[8]);
        int end = Integer.parseInt(values[9]);
        nodeDistGraph[from][to] = nodeDistGraph[to][from] = t_cost;
        nodeTimeGraph[from][to] = nodeTimeGraph[to][from] = t_time;
        Task t = new Task(values[0], Type.Arc, demand, from, to, t_cost, s_cost, t_time, s_time, begin, end);
        Task tr = new Task(values[0], Type.Arc, demand, to, from, t_cost, s_cost, t_time, s_time, begin, end);
        allTasks[requiredN + requiredA + index] = t;
        allTasks[requiredN + requiredA + requiredE + index] = tr;
        edgeTasks[index] = t;
        edgeReverseTasks[index] = tr;
    }

    private void createArcTask(String[] values, int index) {
        int from = Integer.parseInt(values[1]);
        int to = Integer.parseInt(values[2]);
        int t_cost = Integer.parseInt(values[3]);
        int demand = Integer.parseInt(values[4]);
        int s_cost = Integer.parseInt(values[5]);
        int t_time = Integer.parseInt(values[6]);
        int s_time = Integer.parseInt(values[7]);
        int begin = Integer.parseInt(values[8]);
        int end = Integer.parseInt(values[9]);
        nodeDistGraph[from][to] = t_cost;
        nodeTimeGraph[from][to] = t_time;
        Task t = new Task(values[0], Type.Arc, demand, from, to, t_cost, s_cost, t_time, s_time, begin, end);
        allTasks[requiredN + index] = t;
        arcTasks[index] = t;
    }

    private void completeGraph(String[] values) {
        int from = Integer.parseInt(values[1]);
        int to = Integer.parseInt(values[2]);
        int t_cost = Integer.parseInt(values[3]);
        int t_time = Integer.parseInt(values[4]);
        nodeDistGraph[from][to] = t_cost;
        nodeTimeGraph[from][to] = t_time;
        if (values[0].startsWith("NrE")) {
            nodeDistGraph[to][from] = t_cost;
            nodeTimeGraph[to][from] = t_time;
        }
    }

    @Override
    public String toString() {
        return "Data =>\n\t" +
                "instName=" + instName + "\n\t" +
                "optimalValue=" + optimalValue + "\n\t" +
                "vehicles=" + vehicles + "\n\t" +
                "capacity=" + capacity + "\n\t" +
                "depotNode=" + depotNode + "\n\t" +
                "nodes=" + nodes + "\n\t" +
                "arcs=" + arcs + "\n\t" +
                "edges=" + edges + "\n\t" +
                "requiredN=" + requiredN + "\n\t" +
                "requiredA=" + requiredA + "\n\t" +
                "requiredE=" + requiredE + '\n';
    }
}
