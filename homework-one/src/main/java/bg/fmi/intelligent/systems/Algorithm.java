package bg.fmi.intelligent.systems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Algorithm {

    private static int[][] testMatrix = {
            {1, 2, 3},
            {4, 5, 6},
            {0, 7, 8}
    };
//    private static int[][] testMatrix = {
//            {1, 2, 3},
//            {4, 7, 5},
//            {0, 6, 8}
//    };


    private static int finalNeutralPosition = testMatrix.length * testMatrix.length - 1;
//    private static int finalNeutralPosition = 4;

    public static void main(String[] args) {
        if (false) {
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

        Configuration intial = new Configuration(testMatrix,
                algorithm.calculateHeuristic(testMatrix));

        PriorityQueue<Configuration> priorityQueue = new PriorityQueue<>(Configuration::compareTo);
        priorityQueue.add(intial);

        Configuration result = null;

        int threshold = 5;

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

    private int manhattanDistance(int[][] matrix) {
        int dimension = matrix.length * matrix.length;
        int currentRow = 0;
        int currentColumn = 0;

        int heuristic = 0;

        for (int element = 1; element < dimension; ++element) {

            if (element == finalNeutralPosition && finalNeutralPosition != dimension - 1) {
                currentColumn++;
                if (currentColumn == matrix.length) {
                    currentColumn = 0;
                    currentRow++;
                }
            }

            for (int i = 0; i < matrix.length; ++i) {
                for (int j = 0; j < matrix.length; ++j) {

                    if (matrix[i][j] == element) {
                        heuristic += Math.abs(i - currentRow) + Math.abs(j - currentColumn);
                        currentColumn++;
                        if (currentColumn == matrix.length) {
                            currentColumn = 0;
                            currentRow++;
                        }
                        break;
                    }

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
        if (configuration != null && configuration.getHeuristicValue() < threshold) {
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
}
