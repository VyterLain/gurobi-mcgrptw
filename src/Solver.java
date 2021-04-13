import gurobi.*;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    public final Data data;
    public Solution solution;
    public int vars;
    public int constr;
    public double node;
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
            // w_ik, start time of task i at vehicle k
            GRBVar[][] w = new GRBVar[data.allTasks.length + 2][k];
            // set all w_ik
            for (int k = 0; k < this.k; k++)
                for (int i = 0; i < w.length; i++)
                    w[i][k] = model.addVar(0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "w_{" + i + ',' + k + '}');
            // when j == i, x = 0
            // when j == i', x = 0
            // when j == 0, x = 0
            // when i == n+1, x = 0
            for (int k = 0; k < this.k; k++) {
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x[i].length; j++) {
                        if (i == j
                                || (i > data.requiredN + data.requiredA && i <= data.requiredN + data.requiredA + data.requiredE && j == i + 4)
                                || (i > data.requiredN + data.requiredA + data.requiredE && i <= data.requiredN + data.requiredA + 2 * data.requiredE && j == i - 4)
                                || (j == 0)
                                || (i == x.length - 1)) {
                            GRBLinExpr expr = new GRBLinExpr();
                            expr.addTerm(1, x[i][j][k]);
                            model.addConstr(expr, GRB.EQUAL, 0, "x_{" + i + ',' + j + ',' + k + "} = 0");
                        }
                    }
                }
            }
            // TODO: other constr
            // set objective
            GRBLinExpr obj = new GRBLinExpr();
            for (int i = 0; i < x.length; i++) {
                for (int j = 0; j < x[i].length; j++) {
                    for (int k = 0; k < x[i][j].length; k++) {
                        // when i == n+1, x should be 0
                        // when j == 0, x should be 0
                        // we use big_num to check if there is something wrong
                        if (i == x.length - 1 || j == 0) obj.addTerm(Data.BIG_NUM, x[i][j][k]);
                            // when j == n+1, it means next task is depot
                        else if (j == x[i].length - 1) obj.addTerm(data.nodeDistGraph[i][0], x[i][j][k]);
                            // add c_{i,j}*x_{i,j,k}
                        else obj.addTerm(data.nodeDistGraph[i][j], x[i][j][k]);
                    }
                }
            }
            // add all travel cost for serving one task
            // this task must not be depot and nodes
            for (int i = 0; i < x.length; i++) {
                for (int j = 0; j < x[i].length; j++) {
                    for (int k = 0; k < x[i][j].length; k++) {
                        if (i != 0 && i != x.length - 1) obj.addTerm(data.allTasks[i - 1].t_cost, x[i][j][k]);
                    }
                }
            }
            // this is for checking, because the travel cost is fixed(tasks will be only served once)
//            GRBVar constant1 = model.addVar(1, 1, 0, GRB.INTEGER, "constant for fixed travel cost");
//            obj.addTerm(data.getAllDemandedDist(), constant1);
            model.setObjective(obj, GRB.MINIMIZE);
            // Optimize model
            model.optimize();
            // the result info
            time = model.get(GRB.DoubleAttr.Runtime);
            vars = model.get(GRB.IntAttr.NumVars);
            constr = model.get(GRB.IntAttr.NumConstrs);
            int status = model.get(GRB.IntAttr.Status);
            if (status != GRB.Status.INFEASIBLE) {
                node = model.get(GRB.DoubleAttr.NodeCount);
                UB = -1;
                LB = model.get(GRB.DoubleAttr.ObjBound);
                gap = -1;
                if (model.get(GRB.IntAttr.SolCount) != 0) {
                    UB = model.get(GRB.DoubleAttr.ObjVal);
                    gap = model.get(GRB.DoubleAttr.MIPGap);
                    // get solution
                    solution = getSolution(x);
                } else solution = new Solution(data, "not found");
            } else solution = new Solution(data, "infeasible");
            // end gurobi
            model.dispose();
            env.dispose();
        } catch (GRBException e) {
            e.printStackTrace();
        }
    }

    private Solution getSolution(GRBVar[][][] x) throws GRBException {
        List<Vehicle> vehicles = new ArrayList<>();
        for (int k = 0; k < this.k; k++) {
            List<Task> tasks = new ArrayList<>();
            int last = 0;
            while (last != x[last].length - 1) {
                for (int j = 0; j < x[last].length; j++) {
                    if (x[last][j][k].get(GRB.DoubleAttr.X) == 1) {
                        if (last != 0) {
                            tasks.add(data.allTasks[last - 1]);
                            System.out.println("find next task => " + data.allTasks[last - 1]);
                        }
                        last = j;
                        break;
                    }
                }
            }
            tasks.remove(0);
            Vehicle v = new Vehicle(data, k + 1, (Task[]) tasks.toArray());
            vehicles.add(v);
        }
        return new Solution(data, (Vehicle[]) vehicles.toArray());
    }

    private void init() {
        k = data.requiredA + data.requiredN + data.requiredE;
    }

    @Override
    public String toString() {
        return data.instName + ',' + vars + ',' + constr + ',' + node + ',' + time + ',' + UB + ',' + LB + ',' + gap;
    }
}
