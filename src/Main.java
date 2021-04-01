import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Data data = IO.ReadData("src/data/TWA10A_t.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
