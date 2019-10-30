package bg.fmi.intelligent.systems.n.queens;

public class Algorithm {
    public static void main(String[] args) {

        int size = 1000;

        while (true) {
            long startingTime = System.currentTimeMillis();
            Configuration configuration = new Configuration(size);
            boolean isCorrect = false;
            try {
                for (int i = 0; i < 170; ++i) {
                    isCorrect = !configuration.makeMove();
                    if (isCorrect) {
                        break;
                    }
                }
            } catch (RuntimeException e) {
                continue;
            }

            if (isCorrect) {
                System.out.println("Estimated time: " + (System.currentTimeMillis() - startingTime));
                for (int i = 0; i < configuration.getQueens().length; ++i) {
                    for (int j = 0; j < configuration.getQueens().length; ++j) {
                        if (configuration.getQueens()[i] == j) {
                            System.out.print("*");
                        } else {
                            System.out.print(".");
                        }
                    }
                    System.out.println();
                }
                break;
            }
        }
    }
}
