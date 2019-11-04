package bg.fmi.intelligent.systems.n.queens;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;

public class Configuration {
    public static final Random random = new Random();

    private int size;
    private int[] queens;
    private int[] columns;
    private int[] diagonalFirst;
    private int[] diagonalSecond;
    private List<Conflict> minConflictQueens = new ArrayList<>();
    private List<Conflict> maxConflictQueens = new ArrayList<>();

    public Configuration(int size) {

        queens = new int[size];
        columns = new int[size];
        diagonalFirst = new int[2 * size - 1];
        diagonalSecond = new int[2 * size - 1];


        List<Integer> shuffledColumns = IntStream.range(0,size).boxed().collect(Collectors.toList());
        Collections.shuffle(shuffledColumns);

        for (int i = 0; i < shuffledColumns.size(); ++i) {
            int currentColumn = shuffledColumns.get(i);
            queens[i] = currentColumn;
            columns[currentColumn]++;
            diagonalFirst[size - 1 - currentColumn + i]++;
            diagonalSecond[currentColumn + i]++;
        }
        this.size = size;
    }

    public int[] getQueensPlace() {
        boolean hasFoundSolution = false;
        for (int i = 0; i < 2 * size; ++i) {
            int row = getRowWithMaxConflicts();
            if (row == -1) {
                hasFoundSolution = true;
                break;
            }

            int previousColumn = queens[row];
            int nextColumn = getColumnWithMinConflicts(row);

            queens[row] = nextColumn;

            columns[previousColumn]--;
            columns[nextColumn]++;

            diagonalFirst[size - 1 - previousColumn + row]--;
            diagonalFirst[size - 1 - nextColumn + row]++;

            diagonalSecond[previousColumn + row]--;
            diagonalSecond[nextColumn + row]++;
        }

        if (hasFoundSolution) {
            return queens;
        }
        return null;
    }

    private int getColumnWithMinConflicts(int row) {
        minConflictQueens.clear();

        for (int column = 0; column < size; ++column) {
            int candidate =
                    columns[column] + diagonalFirst[size - 1 - column + row] + diagonalSecond[row
                            + column];

            if (minConflictQueens.isEmpty() || minConflictQueens.get(0).value == candidate) {
                minConflictQueens.add(new Conflict(column, candidate));
            } else if (minConflictQueens.get(0).value > candidate) {
                minConflictQueens.clear();
                minConflictQueens.add(new Conflict(column, candidate));
            }
        }

        return minConflictQueens.get(random.nextInt( minConflictQueens.size())).place;
    }

    private int getRowWithMaxConflicts() {
        maxConflictQueens.clear();

        for (int row = 0; row < size; ++row) {
            int column = queens[row];
            int candidate =
                    columns[column] + diagonalFirst[size - 1 - column + row] + diagonalSecond[row
                            + column] - 3;

            if (maxConflictQueens.isEmpty() || maxConflictQueens.get(0).value == candidate) {
                maxConflictQueens.add(new Conflict(row, candidate));
            } else if (maxConflictQueens.get(0).value < candidate) {
                maxConflictQueens.clear();
                maxConflictQueens.add(new Conflict(row, candidate));
            }
        }

        if (maxConflictQueens.get(0).value == 0) {
            return -1;
        }


        return maxConflictQueens.get(random.nextInt(maxConflictQueens.size())).place;
    }

    @AllArgsConstructor
    private static class Conflict {
        int place;
        int value;
    }
}
