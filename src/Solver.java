import gurobi.*;

public class Solver {

    public final Data data;
    public Solution solution;
    public int vars;
    public int constr;
    public int node;
    public double time;
    public double UB;
    public double LB;
    public double gap;
    private int k;

    public Solver(Data data, double time) {
        this.data = data;
        this.time = time;
        init();
    }

    //TODO
    public void run() {
        try {
            // set environment
            GRBEnv env = new GRBEnv(true);
            env.set("LogFile", "mcgrptw-gurobi.log");
            env.start();
            // create model
            GRBModel model = new GRBModel(env);
            model.set(GRB.DoubleParam.TimeLimit, time);
            // create variables
            // x_ijk, from task i to j at vehicle k
            // index 0, n+1 => depot
            // index n => task ( node arc edge edge-revers )
            GRBVar[][][] x = new GRBVar[data.allTasks.length + 2][data.allTasks.length + 2][k];
            // set all x_ijk in {0,1}
            for (int i = 0; i < x.length; i++)
                for (int j = 0; j < x[i].length; j++)
                    for (int k = 0; k < x[i][j].length; k++)
                        x[i][j][k] = model.addVar(0, 1, 0, GRB.BINARY, "x_{" + i + ',' + j + ',' + k + '}');
            // when j == i, x = 0
            // when j == i', x = 0
            for (int k = 0; k < this.k; k++) {
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x[i].length; j++) {
                        if (i == j
                                || (i > data.requiredN + data.requiredA && i <= data.requiredN + data.requiredA + data.requiredE && j == i + 4)
                                || (i > data.requiredN + data.requiredA + data.requiredE && i <= data.requiredN + data.requiredA + 2 * data.requiredE && j == i - 4)) {
                            GRBLinExpr expr = new GRBLinExpr();
                            expr.addTerm(1, x[i][j][k]);
                            model.addConstr(expr, GRB.EQUAL, 0, "x_{" + i + ',' + j + ',' + k + "} = 0");
                        }
                    }
                }
            }
            // end gurobi
            model.dispose();
            env.dispose();
        } catch (GRBException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // TODO: init some parameters or something maybe, like the max k
        k = data.requiredA + data.requiredN + data.requiredE;
    }

    @Override
    public String toString() {
        return data.instName + ',' + vars + ',' + constr + ',' + node + ',' + time + ',' + UB + ',' + LB + ',' + gap;
    }

    private class Turple<A, B> {
        A _1;
        B _2;

        public Turple(A a, B b) {
            _1 = a;
            _2 = b;
        }
    }
}
