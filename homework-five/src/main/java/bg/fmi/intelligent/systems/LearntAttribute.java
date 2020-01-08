package bg.fmi.intelligent.systems;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LearntAttribute {
    private double probYesDemocrats;
    private double probNoDemocrats;
    private double probYesRepublican;
    private double probNoRepublican;
}
