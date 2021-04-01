import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Data[] allData = IO.ReadAllData("src/data");
            for (Data data : allData) System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
