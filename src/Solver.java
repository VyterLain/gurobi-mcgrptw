public class Solver {

    public final Data data;
    public Solution solution;

    public Solver(Data data) {
        this.data = data;
        init();
    }

    //TODO
    public void run() {

    }

    private void init() {
        // TODO: init some parameters or something maybe, like the max k
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
