package bg.fmi.intelligent.systems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Algorithm {

        private static int[][] testMatrix = {
            {2, 3, 9, 7},
            {10, 0, 14, 13},
            {8, 1, 5, 4},
            {11, 12, 15, 6}
    };

    private static Position[] goalPositions;
    private static int finalNeutralPosition = testMatrix.length * testMatrix.length;

    public static void main(String[] args) {
        if (true) {
            Scanner scanner = new Scanner(System.in);

            int elements = scanner.nextInt();
            finalNeutralPosition = scanner.nextInt();

            int length = (int) Math.sqrt(elements + 1);
            testMatrix = new int[length][length];
            for (int i = 0; i < length; ++i) {
                for (int j = 0; j < length; ++j) {
                    testMatrix[i][j] = scanner.nextInt();
                }
            }
        }

        Algorithm algorithm = new Algorithm();
        algorithm.setGoalPositions();

        Configuration initial = new Configuration(testMatrix,
                algorithm.calculateHeuristic(testMatrix));

        PriorityQueue<Configuration> priorityQueue = new PriorityQueue<>(Configuration::compareTo);
        priorityQueue.add(initial);

        Configuration result = null;

        int threshold = 500;

        while (result == null) {
            while (!priorityQueue.isEmpty()) {
                Configuration current = priorityQueue.poll();
                if (current.getHeuristicValue() == 0) {
                    result = current;
                    break;
                }
                priorityQueue.addAll(algorithm.getChildren(current, threshold));
            }
            threshold++;

        }

        System.out.println("Final matrix: ");
        int[][] matrix = result.getMatrix();
        for (int[] matrixRow : matrix) {
            for (int j = 0; j < matrix.length; ++j) {
                System.out.print(matrixRow[j] + " ");
            }
            System.out.println();
        }


        System.out.println("Steps: " + result.getSteps());

        List<String> directions = result.getDirections();

        directions.forEach(direction -> System.out.println("Direction: " + direction));
    }

    public int calculateHeuristic(int[][] matrix) {
        return manhattanDistance(matrix);
    }

    private void setGoalPositions() {
        int dimension = testMatrix.length * testMatrix.length;
        goalPositions = new Position[dimension];

        int currentRow = 0;
        int currentColumn = 0;

        boolean hasAddedNeutralPosition = false;
        for (int i = 1; i < dimension; ++i) {
            Position current = new Position();
            current.row = currentRow;
            current.column = currentColumn;

            if (i == finalNeutralPosition && !hasAddedNeutralPosition) {
                goalPositions[0] = current;
                --i;
                hasAddedNeutralPosition = true;
            } else {
                goalPositions[i] = current;
            }

            currentColumn++;
            if (currentColumn == testMatrix.length) {
                currentColumn = 0;
                currentRow++;
            }
        }

        if (finalNeutralPosition == -1) {
            Position emptyPosition = new Position();
            emptyPosition.row = testMatrix.length - 1;
            emptyPosition.column = testMatrix.length - 1;
            goalPositions[0] = emptyPosition;
        }
    }

    private int manhattanDistance(int[][] matrix) {
        int heuristic = 0;

         for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix.length; ++j) {
                int element = matrix[i][j];
                if (element != Configuration.EMPTY_FIELD) {
                    heuristic += Math.abs(i - goalPositions[element].row)
                            + Math.abs(j - goalPositions[element].column);
                }
            }
        }

        return heuristic;
    }

    public List<Configuration> getChildren(Configuration parent, int threshold) {
        List<Configuration> children = new ArrayList<>();

        Moves forbiddenMove = parent.getLastMove().getTheOpposite();


        if (forbiddenMove != Moves.UP) {
            Configuration child = generateChild(parent, Moves.UP);
            addIfNotNullWithThreshold(children, child, threshold);
        }
        if (forbiddenMove != Moves.DOWN) {
            Configuration child = generateChild(parent, Moves.DOWN);
            addIfNotNullWithThreshold(children, child, threshold);
        }
        if (forbiddenMove != Moves.LEFT) {
            Configuration child = generateChild(parent, Moves.LEFT);
            addIfNotNullWithThreshold(children, child, threshold);
        }
        if (forbiddenMove != Moves.RIGHT) {
            Configuration child = generateChild(parent, Moves.RIGHT);
            addIfNotNullWithThreshold(children, child, threshold);
        }
        return children;
    }

    private void addIfNotNullWithThreshold(List<Configuration> list, Configuration configuration,
            int threshold) {
        if (configuration != null
                && configuration.getHeuristicValue() + configuration.getSteps() < threshold) {
            list.add(configuration);
        }
    }

    private Configuration generateChild(Configuration parent, Moves currentMove) {
        int x = 0;
        int y = 0;

        switch (currentMove) {
            case UP:
                y = -1;
                break;
            case DOWN:
                y = 1;
                break;
            case LEFT:
                x = -1;
                break;
            case RIGHT:
                x = 1;
                break;
        }

        int nextNeutralRow = parent.getNeutralPositionRow() + y;
        int nextNeutralColumn = parent.getNeutralPositionColumn() + x;

        int[][] matrix = transformed(
                parent.getMatrix(),
                parent.getNeutralPositionRow(),
                parent.getNeutralPositionColumn(),
                nextNeutralRow,
                nextNeutralColumn);

        Configuration child = null;
        if (matrix != null) {
            int heuristicValue = calculateHeuristic(matrix);

            List<String> childDirections = new ArrayList<>(parent.getDirections());
            childDirections.add(currentMove.getTheOpposite().toString());

            child = Configuration.builder()
                    .matrix(matrix)
                    .heuristicValue(heuristicValue)
                    .neutralPositionRow(nextNeutralRow)
                    .neutralPositionColumn(nextNeutralColumn)
                    .lastMove(currentMove)
                    .steps(parent.getSteps() + 1)
                    .directions(childDirections)
                    .build();
        }
        return child;
    }

    private int[][] transformed(int[][] matrix, int row, int column, int nextRow, int nextColumn) {
        int[][] transformed = Arrays.stream(matrix)
                .map(int[]::clone)
                .toArray(int[][]::new);

        if (nextRow < 0 || nextRow >= matrix.length
                || nextColumn < 0 || nextColumn >= matrix.length) {
            return null;
        }

        transformed[row][column] = transformed[nextRow][nextColumn];
        transformed[nextRow][nextColumn] = Configuration.EMPTY_FIELD;

        return transformed;
    }

    private static class Position {
        int row;
        int column;
    }
}
