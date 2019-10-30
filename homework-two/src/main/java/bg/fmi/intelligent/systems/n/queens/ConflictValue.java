package bg.fmi.intelligent.systems.n.queens;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConflictValue {
    int queen;
    int maxValue;
    int position;
    boolean foundSolution;
}