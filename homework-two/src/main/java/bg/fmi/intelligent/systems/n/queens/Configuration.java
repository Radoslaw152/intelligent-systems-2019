package bg.fmi.intelligent.systems.n.queens;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        List<Integer> mins = new ArrayList<>();
        for (int i = 0; i < queens.length; ++i) {
            int[] queenConflicts = conflicts[i];
            int min = Arrays.stream(queenConflicts)
                    .min()
                    .orElseThrow(RuntimeException::new);

            for(int j = 0; j < queenConflicts.length;++j) {
                if(min == queenConflicts[j]) {
                    mins.add(j);
                }
            }

            int position = mins.get(getRandom(mins.size()));
            mins.clear();

            queens[i] = position;
            increaseConflict(i);
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
            if(i != y) {
                conflicts[x][i] += toAdd;
            }
            if(i != x) {
                conflicts[i][y] += toAdd;
            }

            if (x > i && y > i) {
                conflicts[x - i - 1][y - i - 1] += toAdd;
            }
            if (x > i && y + i + 1 < queens.length) {
                conflicts[x - i - 1][y + i + 1] += toAdd;
            }
            if (x + i + 1 < queens.length && y > i) {
                conflicts[x + i + 1][y - i - 1] += toAdd;
            }
            if (x + i + 1 < queens.length && y + i + 1 < queens.length) {
                conflicts[x + i + 1][y + i + 1] += toAdd;
            }
        }

    }

    public static int getRandom(int bound) {
        return random.nextInt(bound);
    }
}
