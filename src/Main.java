import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Data[] allData = IO.ReadAllData("src/data");
//            for (Data data : allData) System.out.println(data);
            Data sample = allData[0];
            Solver solver = new Solver(sample, 60);
            solver.run();
            Solution solution = solver.solution;
            System.out.println(solution);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
