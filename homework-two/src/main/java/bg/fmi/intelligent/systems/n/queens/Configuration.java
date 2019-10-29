package bg.fmi.intelligent.systems.n.queens;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
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

            for (int j = 0; j < queenConflicts.length; ++j) {
                if (min == queenConflicts[j]) {
                    mins.add(j);
                }
            }

            int position = mins.get(getRandom(mins.size()));
            mins.clear();

            queens[i] = position;
            increaseConflict(i);
        }
    }

    private void increaseConflict(int queen) {
        changeConflict(queen, 1);
    }

    private void decreaseConflict(int queen) {
        changeConflict(queen, -1);
    }

    private void changeConflict(int queen, int toAdd) {
        int x = queen;
        int y = queens[queen];

        for (int i = 0; i < queens.length; ++i) {
            if (i != y) {
                conflicts[x][i] += toAdd;
            }
            if (i != x) {
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

    private void changeQueensPlace(int queen, int position) {
        decreaseConflict(queen);

        queens[queen] = position;

        increaseConflict(queen);
    }

    private static int getRandom(int bound) {
        return random.nextInt(bound);
    }

    private ConflictValue findBestQueen() {
        List<ConflictValue> possibleQueens = new ArrayList<>();
        for (int i = 0; i < conflicts.length; ++i) {
            int currentConflictValue = conflicts[i][queens[i]];

            int minConflict = Arrays.stream(conflicts[i])
                    .min()
                    .orElseThrow(RuntimeException::new);

            if (currentConflictValue - minConflict == 0) {
                continue;
            }

            List<ConflictValue> allMinValues = new ArrayList<>();
            for (int j = 0; i < conflicts[i].length; ++j) {
                if (minConflict == conflicts[i][j]) {
                    ConflictValue value =
                            new ConflictValue(i, currentConflictValue - minConflict, j);
                    allMinValues.add(value);
                }
            }

            if (possibleQueens.isEmpty()) {
                possibleQueens.addAll(allMinValues);
            } else if (possibleQueens.get(0).maxValue == currentConflictValue - minConflict) {
                possibleQueens.addAll(allMinValues);
            } else if (possibleQueens.get(0).maxValue < currentConflictValue - minConflict) {
                possibleQueens.clear();
                possibleQueens.addAll(allMinValues);
            }
        }

        if (possibleQueens.isEmpty()) {
            throw new RuntimeException("No solution");
        }

        return  possibleQueens.get(getRandom(possibleQueens.size()));
    }

    public void makeMove() {
        ConflictValue value = findBestQueen();
        changeQueensPlace(value.getQueen(),value.getPosition());
    }

    public boolean checkIfCorrect() {
        for(int i = 0; i < queens.length;++i) {
            if(conflicts[i][queens[i]] != 0) {
                return false;
            }
        }
        return true;
    }
}
