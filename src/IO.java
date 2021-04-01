import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IO {
    public static Data[] ReadAllData(String dirPath) throws IOException {
        File f = new File(dirPath);
        String[] names = f.list();
        assert names != null;
        Data[] data = new Data[names.length];
        for (int i = 1; i < names.length; i++) data[i] = ReadData(dirPath + '/' + names[i]);
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
        for (int i = 0; i < strings.size(); i++) data.initTasksInfo(strings.remove(0).split("\t"), i);
        return data;
    }

    public static void WriteData(Solver solve) throws IOException {
        // TODO
    }
}
