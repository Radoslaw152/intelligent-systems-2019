package bg.fmi.intelligent.systems;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
public class Algorithm {
    public static void main(String[] args) {
        int N = 1000;

        int[][] matrix = generateRandom(N);

        Set<Path> paths = new HashSet<>();
        while (paths.size() < N) {
            paths.add(new Path(matrix));
        }


        int times = 0;

        do {
            List<Path> bests = paths.stream()
                    .sorted(Comparator.comparingInt(Path::getDistance))
                    .limit((int)(N * 0.3))
                    .collect(Collectors.toList());

            bests.forEach(path -> System.out.println(path.getDistance()));

            System.out.println("---------------------------------");

            int min = 0;
            do {
                Path randomFirst = bests.get(Path.getRandom(bests.size()));
                Path randomSecond = bests.get(Path.getRandom(bests.size()));

                if (randomFirst == randomSecond) {
                    continue;
                }

                Crossover crossover = new Crossover(randomFirst, randomSecond);
                crossover = crossover.crossPaths();

                Path newFirst = crossover.getFirst();
                Path newSecond = crossover.getSecond();

                newFirst.mutate();
                newSecond.mutate();

                paths.add(newFirst);
                paths.add(newSecond);
                min++;
            } while (min < N * 0.3);

            paths = paths.stream()
                    .sorted(Comparator.comparingInt(Path::getDistance))
                    .limit(N)
                    .collect(Collectors.toSet());

            times++;
        } while (times < N);
    }

    private static int[][] generateRandom(int size) {
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; ++i) {
            matrix[i][i] = 0;
            for (int j = i + 1; j < size; ++j) {
                matrix[i][j] = Path.getRandom(2 * size);
                matrix[j][i] = matrix[i][j];
            }
        }
        return matrix;
    }
}
