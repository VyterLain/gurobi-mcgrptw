import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IO {
    public static Data[] ReadAllData(String dirPath) throws IOException {
        File f = new File(dirPath);
        String[] names = f.list();
        assert names != null;
        Data[] data = new Data[names.length];
        for (int i = 0; i < names.length; i++) data[i] = ReadData(dirPath + '/' + names[i]);
        return data;
    }

    public static Data ReadData(String datPath) throws IOException {
        File f = new File(datPath);
        BufferedReader br = new BufferedReader(new FileReader(f));
        List<String> strings = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) strings.add(line);
        br.close();
        strings.removeIf(s -> s.equals("") || s.endsWith("TIME"));
//        strings.forEach(System.out::println);
        Data data = new Data();
        data.instName = strings.remove(0).split(":")[1].trim();
        for (int i = 1; i <= 10; i++) data.initBasicInfo(Integer.parseInt(strings.remove(0).split(":")[1].trim()), i);
//        strings.forEach(System.out::println);
        data.initArray();
        for (int i = 0; i < strings.size(); i++) data.initTasksInfo(strings.get(i).split("\t"), i);
        OutputDistMatrix(data, "raw");
        OutputTimeMatrix(data, "raw");
        data.preprocess();
        OutputDistMatrix(data, "processed");
        OutputTimeMatrix(data, "processed");
        return data;
    }

    public static void WriteData(Solver[] solvers) throws IOException {
        File csv = new File("src/output/result/stat.csv");
        BufferedWriter bw_csv = new BufferedWriter(new FileWriter(csv));
        bw_csv.write("inst name,vars,constr,node,time,UB,LB,gap");
        for (Solver solver : solvers) {
            bw_csv.write(solver.toString());
            File txt = new File("src/output/result/" + solver.data.instName + ".txt");
            BufferedWriter bw_txt = new BufferedWriter(new FileWriter(txt));
            bw_txt.write(solver.solution.toString());
            bw_txt.close();
        }
        bw_csv.close();
    }

    private static void OutputDistMatrix(Data data, String prefix) throws IOException {
        StringBuilder sb = new StringBuilder(data.toString());
        for (int row = 0; row < data.nodeDistGraph.length; row++) {
            for (int col = 0; col < data.nodeDistGraph[row].length; col++) {
                int num = data.nodeDistGraph[row][col];
                if ((data.nodeDistGraph[row][col] >= Data.BIG_NUM || data.nodeDistGraph[row][col] <= 0) && row != col)
                    num = -1;
                sb.append(num).append("\t\t");
            }
            sb.append('\n');
        }
        File f = new File("src/output/dist/" + prefix + "/" + data.instName + ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write(sb.toString());
        bw.close();
    }

    private static void OutputTimeMatrix(Data data, String prefix) throws IOException {
        StringBuilder sb = new StringBuilder(data.toString());
        for (int row = 0; row < data.nodeTimeGraph.length; row++) {
            for (int col = 0; col < data.nodeTimeGraph[row].length; col++) {
                int num = data.nodeTimeGraph[row][col];
                if ((data.nodeTimeGraph[row][col] >= Data.BIG_NUM || data.nodeTimeGraph[row][col] <= 0) && row != col)
                    num = -1;
                sb.append(num).append("\t\t");
            }
            sb.append('\n');
        }
        File f = new File("src/output/time/" + prefix + "/" + data.instName + ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write(sb.toString());
        bw.close();
    }
}
