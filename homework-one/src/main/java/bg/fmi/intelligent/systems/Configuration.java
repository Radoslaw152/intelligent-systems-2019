package bg.fmi.intelligent.systems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Configuration implements Comparable<Configuration>{
    public static final int EMPTY_FIELD = 0;

    int[][] matrix;
    int heuristicValue;
    int steps;
    int neutralPositionRow;
    int neutralPositionColumn;
    Moves lastMove;
    Configuration parentConfiguration;

    public Configuration(int[][] matrix, int heuristicValue) {
        this(matrix, heuristicValue, 0,
                0,0,
                Moves.NEUTRAL, null);

        while (neutralPositionRow < matrix.length) {
            neutralPositionColumn = 0;

            boolean found = false;
            while (neutralPositionColumn < matrix.length) {
                if (matrix[neutralPositionRow][neutralPositionColumn] == EMPTY_FIELD) {
                    found = true;
                    break;
                }
                ++neutralPositionColumn;
            }

            if (found) {
                break;
            }
            ++neutralPositionRow;
        }

    }

    @Override
    public int compareTo(Configuration o) {
        return (heuristicValue + steps) - (o.getHeuristicValue() + o.getSteps());
    }
}

