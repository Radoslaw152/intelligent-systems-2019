package bg.fmi.intelligent.systems;

import lombok.Data;

@Data
public class ClassType {
    public static final String DEMOCRAT = "democrat";
    public static final String REPUBLICAN = "republican";
    public static final String[] ATTRIBUTES = {
            "handicapped-infants",
            "water-project-cost-sharing",
            "adoption-of-the-budget-resolution",
            "physician-fee-freeze",
            "el-salvador-aid",
            "religious-groups-in-schools",
            "anti-satellite-test-ban",
            "aid-to-nicaraguan-contras",
            "mx-missile",
            "immigration",
            "synfuels-corporation-cutback",
            "education-spending",
            "superfund-right-to-sue",
            "crime",
            "duty-free-exports",
            "export-administration-act-south-africa"
    };
    public static final int ATTRIBUTES_LENGTH = 16;
    private String className;
    private Boolean[] attributes;

    public static ClassType createClassType(String line) {
        String[] arr = line.split(",");
        ClassType result = new ClassType();
        result.setClassName(arr[0]);
        result.setAttributes(new Boolean[ATTRIBUTES_LENGTH]);
        for(int i = 1; i < arr.length;++i) {
            if(arr[i].equals("y")) {
                result.getAttributes()[i - 1] = true;
            } else if (arr[i].equals("n")){
                result.getAttributes()[i - 1] = false;
            } else {
                result.getAttributes()[i - 1] = null;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(className).append(",");
        for(Boolean attribute : attributes) {
            if(attribute == null) {
                builder.append("?,");
            } else if(attribute) {
                builder.append("y,");
            } else {
                builder.append("n,");
            }
        }
        return builder.toString();
    }
}
