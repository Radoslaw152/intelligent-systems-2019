package bg.fmi.intelligent.systems.n.queens;

public class Algorithm {
    public static void main(String[] args) {

        int size = 3;

        while (true) {
            Configuration configuration = new Configuration(size);
            boolean isCorrect = false;
            try {
                for (int i = 0; i < 150; ++i) {
                    configuration.makeMove();
                    isCorrect = configuration.checkIfCorrect();
                    if (isCorrect) {
                        break;
                    }
                }
            } catch (RuntimeException e) {
                continue;
            }

            if (isCorrect) {
                for (int i = 0; i < configuration.getQueens().length; ++i) {
                    for (int j = 0; j < configuration.getQueens().length; ++j) {
                        if (configuration.getQueens()[j] == j) {
                            System.out.print("*");
                        } else {
                            System.out.print("_");
                        }
                    }
                    System.out.println();
                }
            }
        }
    }
}
