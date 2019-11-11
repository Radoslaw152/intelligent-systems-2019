package bg.fmi.intelligent.systems;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Getter;

@Getter
public class Path {
    private final static Random random = new Random(System.currentTimeMillis());
    private int[][] matrix;
    private int[] path;
    private int distance;

    public Path(int[][] matrix, int[] path) {
        this.matrix = matrix;
        this.path = path;
        calculateDistance();
    }

    public Path(int[][] matrix) {
        this.matrix = matrix;
        this.path = new int[matrix.length];

        List<Integer> shuffledCities = IntStream.range(0, matrix.length)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(shuffledCities);

        for (int i = 0; i < path.length; ++i) {
            this.path[i] = shuffledCities.get(i);
        }

        calculateDistance();
    }

    public void mutate() {
        int first = getRandom(path.length);
        int second = getRandom(path.length);

        int temp = path[first];
        path[first] = path[second];
        path[second] = temp;

        calculateDistance();
    }

    private void calculateDistance() {
        this.distance = 0;

        for (int i = 1; i < path.length; ++i) {
            int fromCity = path[i - 1];
            int toCity = path[i];
            this.distance += matrix[fromCity][toCity];
        }
    }

    public static int getRandom(int bound) {
        if (bound <= 0) {
            return 0;
        }
        random.setSeed(System.currentTimeMillis());
        return random.nextInt(bound);
    }

    public void print(PrintStream printStream) {
        for (int i = 0; i < path.length; ++i) {
            printStream.print(path[i] + " ");
        }
        printStream.println();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Path)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        return Arrays.equals(path, ((Path) o).getPath());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(path);
    }
}
