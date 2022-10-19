package h01;

import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

public class H01_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H01")
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
