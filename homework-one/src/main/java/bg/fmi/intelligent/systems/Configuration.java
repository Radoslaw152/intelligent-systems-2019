package bg.fmi.intelligent.systems;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Configuration implements Comparable<Configuration>{
    public static final int EMPTY_FIELD = 0;

    private int[][] matrix;
    private int heuristicValue;
    private int steps;
    private int neutralPositionRow;
    private int neutralPositionColumn;
    private Moves lastMove;
    private List<String> directions;

    public Configuration(int[][] matrix, int heuristicValue) {
        this(matrix, heuristicValue, 0,
                0,0,
                Moves.NEUTRAL, new ArrayList<>());

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

