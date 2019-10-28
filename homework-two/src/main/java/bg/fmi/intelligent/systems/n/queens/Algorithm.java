package bg.fmi.intelligent.systems.n.queens;

public class Algorithm {
    public static void main(String[] args) {
        Configuration config = new Configuration(5);
        for(int i = 0; i < config.getQueens().length;++i) {
            System.out.print(config.getQueens()[i] + " ");
        }
        System.out.println();
        System.out.println("Conflicts");
        for(int i = 0; i < config.getConflicts().length;++i) {
            for(int j = 0; j < config.getConflicts()[i].length;++j) {
                System.out.print(config.getConflicts()[i][j] + " ");
            }
            System.out.println();
        }
    }
}
