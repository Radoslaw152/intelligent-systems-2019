package bg.fmi.intelligent.systems.n.queens;


import java.util.Arrays;
import java.util.Random;

import lombok.Data;

@Data
public class Configuration {
    public static final Random random = new Random();
    public int[] queens;
    public int[][] conflicts;

    public Configuration(int size) {
        queens = new int[size];
        conflicts = new int[size][size];
        for (int[] row : conflicts) {
            for (int i = 0; i < row.length; ++i) {
                row[i] = 0;
            }
        }


        for (int i = 0; i < queens.length; ++i) {
            int[] queenConflicts = conflicts[i];
            int min = Arrays.stream(queenConflicts)
                    .min()
                    .orElseThrow(RuntimeException::new);

            int[] mins = Arrays.stream(queenConflicts)
                    .filter(pos -> pos == min)
                    .toArray();

            int position = mins[getRandom(mins.length)];

            queens[i] = position;
            increaseConflict(position);
        }
    }

    public void increaseConflict(int queen) {
        changeConflict(queen, 1);
    }

    public void decreaseConflict(int queen) {
        changeConflict(queen, -1);
    }

    private void changeConflict(int queen, int toAdd) {
        int x = queen;
        int y = queens[queen];

        for (int i = 0; i < queens.length; ++i) {
            if (i != y) {
                conflicts[x][i] += toAdd;
                conflicts[i][y] += toAdd;
                int diagonal = Math.abs(x - y);
                if (diagonal + i < queens.length) {
                    conflicts[i][Math.abs(x - y) + i] += toAdd;
                }
                if (diagonal - i > -1) {
                    conflicts[i][Math.abs(x - y) - i] += toAdd;
                }
            }
        }

    }

    public static int getRandom(int bound) {
        int number = random.nextInt(bound);
        return number;
    }
}
