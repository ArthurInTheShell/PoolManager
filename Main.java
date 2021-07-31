import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        PoolSolver ps = new PoolSolver();
        for (int i = 1; i <= 10 ; i++) {
            ps.analyse(i);
        }
    }
}
