package GradeCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeCalculator {
    public static double calculateAverage(List<Integer> grades) {
        return Optional.ofNullable(grades)
                .filter(g -> !g.isEmpty())
                .map(g -> g.stream()
                        .filter(grade -> grade != null)
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0))
                .orElse(0.0);
    }
}


