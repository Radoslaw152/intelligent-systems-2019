package bg.fmi.intelligent.systems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        List<ClassType> data = new ArrayList<>();
        Scanner scanner = new Scanner(
                new File(Main.class.getResource("house-votes.data").getPath()));
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            data.add(ClassType.createClassType(line));
        }

        final int k = 10;
        double score = 0;
        Collections.shuffle(data);

        List<List<ClassType>> sets = new ArrayList<>();
        int start = 0;
        int delimiter = data.size() / k;
        int end = delimiter;
        for (int i = 0; i < k; ++i) {
            if(end > data.size()) {
                end = data.size();
            }
            List<ClassType> result = data.subList(start, end);
            sets.add(result);
            start = end;
            end += delimiter;
        }

        for (int i = 0; i < k; ++i) {
            List<ClassType> test = sets.get(i);
            sets.remove(i);
            LearntData learntData = new LearntData(sets, test);
            double accuracity = learntData.accuracity();
            System.out.println(String.format("%s. accuracity - %s", i + 1, accuracity));
            sets.add(i, test);
            score += accuracity;
        }

        System.out.println("Final result: " + (score / k));
    }
}
