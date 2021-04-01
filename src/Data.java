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

    @Override
    public String toString() {
        return "Data =>\n\t" +
                "instName='" + instName + "\n\t" +
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
