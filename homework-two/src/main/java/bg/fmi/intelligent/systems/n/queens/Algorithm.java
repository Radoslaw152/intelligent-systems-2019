package bg.fmi.intelligent.systems.n.queens;

public class Algorithm {
    public static void main(String[] args) {

        int size = 10000;

        long startingTime = System.currentTimeMillis();
        Configuration configuration = new Configuration(size);
        int[] queens = null;
        while (true) {
            queens = configuration.getQueensPlace();
            if (queens == null) {
                configuration = new Configuration(size);
            } else {
                break;
            }
        }

        System.out.println("Estimated time: " + (System.currentTimeMillis() - startingTime));
        for (int i = 0; i < queens.length; ++i) {
            for (int j = 0; j < queens.length; ++j) {
                if (queens[i] == j) {
                    System.out.print("*");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}
