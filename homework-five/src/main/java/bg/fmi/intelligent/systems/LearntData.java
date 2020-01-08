package bg.fmi.intelligent.systems;

import java.util.List;

import lombok.Data;

@Data
public class LearntData {
    private List<List<ClassType>> data;
    private LearntAttribute[] learntData;
    private List<ClassType> testData;
    private double probDemocrat;
    private double probRepublican;

    public LearntData(List<List<ClassType>> data, List<ClassType> testData) {
        this.data = data;
        this.testData = testData;
        probDemocrat = 0;
        probRepublican = 0;
        learntData = new LearntAttribute[ClassType.ATTRIBUTES_LENGTH];
        learn();
    }

    private void learn() {
        double democrats = 0;
        double republicans = 0;
        for (int i = 0; i < learntData.length; ++i) {
            double yesDemocrat = 0;
            double noDemocrat = 0;
            double yesRepublican = 0;
            double noRepublican = 0;
            for (List<ClassType> dataSet : data) {
                for (ClassType classType : dataSet) {
                    if (classType.getClassName().equals(ClassType.DEMOCRAT)) {
                        democrats++;
                    } else {
                        republicans++;
                    }

                    if (classType.getAttributes()[i] == null) {
                        continue;
                    }

                    if (classType.getClassName().equals(ClassType.DEMOCRAT)) {
                        if (classType.getAttributes()[i]) {
                            yesDemocrat++;
                        } else {
                            noDemocrat++;
                        }
                    } else {
                        if (classType.getAttributes()[i]) {
                            yesRepublican++;
                        } else {
                            noRepublican++;
                        }
                    }
                }
                double probYesDemocrats = yesDemocrat / (yesDemocrat + noDemocrat);
                double probNoDemocrats = noDemocrat / (yesDemocrat + noDemocrat);
                double probYesRepublican = yesRepublican / (yesRepublican + noRepublican);
                double probNoRepublican = noRepublican / (yesRepublican + noRepublican);
                learntData[i] = new LearntAttribute(probYesDemocrats, probNoDemocrats,
                        probYesRepublican, probNoRepublican);
            }
        }
        probDemocrat = democrats / (democrats + republicans);
        probRepublican = republicans / (democrats + republicans);
    }

    public double accuracity() {
        double score = 0;
        for (ClassType test : testData) {
            double proportialDemocrat = Math.log(probDemocrat);
            double proportialRepublican = Math.log(probRepublican);
            for (int i = 0; i < learntData.length; ++i) {
                if (test.getAttributes()[i] == null) {
                    continue;
                }
                if (test.getAttributes()[i]) {
                    proportialDemocrat += Math.log(learntData[i].getProbYesDemocrats());
                    proportialRepublican += Math.log(learntData[i].getProbYesRepublican());
                } else {
                    proportialDemocrat += Math.log(learntData[i].getProbNoDemocrats());
                    proportialRepublican += Math.log(learntData[i].getProbNoRepublican());
                }
            }
            if ((proportialDemocrat > proportialRepublican
                    && test.getClassName().equals(ClassType.DEMOCRAT))
                    || (proportialDemocrat < proportialRepublican
                    && test.getClassName().equals(ClassType.REPUBLICAN))) {
                score++;
            }
        }
        return score / testData.size();
    }
}
