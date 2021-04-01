import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IO {
    public static Data ReadData(String path) throws IOException {
        File f = new File(path);
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
        return data;
    }
}
