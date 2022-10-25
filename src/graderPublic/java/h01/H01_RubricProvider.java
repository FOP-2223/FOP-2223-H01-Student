package h01;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import static h01.CheckersTest.LINK;
import static java.util.Arrays.stream;
import static org.sourcegrade.jagr.api.rubric.Grader.testAwareBuilder;
import static org.sourcegrade.jagr.api.rubric.JUnitTestRef.and;
import static org.sourcegrade.jagr.api.rubric.JUnitTestRef.ofMethod;
import static org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers.identical;

public class H01_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H01 | Little Checkers")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("H1.1 | Initialisierung des weißen Steins")
                // 4 Points
                .addChildCriteria(
                    criterion(
                        "Die Position des weißen Steins wird pseudozufällig gewählt.",
                        "testWhiteStoneInitialPosition"
                    ),
                    criterion(
                        "Die Richtung des weißen Steins wird pseudozufällig gewählt.",
                        "testWhiteStoneInitialDirection"
                    ),
                    criterion(
                        "Die Anzahl der Münzen des weißen Steins wird korrekt gewählt.",
                        "testWhiteStoneInitialCoins"
                    ),
                    criterion(
                        "Der weiße Stein wird korrekt initialisiert.",
                        "testWhiteStoneInitialPosition",
                        "testWhiteStoneInitialDirection",
                        "testWhiteStoneInitialCoins"
                    )
                )
                .build()
        ).addChildCriteria(
            Criterion.builder()
                .shortDescription("H1.2 | Initialisierung der schwarzen Steine")
                // 5 Points
                .addChildCriteria(
                    criterion(
                        "Die Position jedes schwarzen Steins wird pseudozufällig gewählt.",
                        "testBlackStonesInitialPositions1"
                    ),
                    criterion(
                        "Die Position jedes schwarzen Steins ist nicht gleich der Position des weißen Steins.",
                        "testBlackStonesInitialPositions2"
                    ),
                    criterion(
                        "Die Richtung jedes schwarzen Steins wird pseudozufällig gewählt.",
                        "testBlackStonesInitialDirections"
                    ),
                    criterion(
                        "Die Anzahl der Münzen jedes schwarzen Steins wird pseudozufällig gewählt.",
                        "testBlackStonesInitialCoins"
                    ),
                    criterion(
                        "Die schwarzen Steine werden korrekt initialisiert.",
                        "testBlackStonesInitialPositions1",
                        "testBlackStonesInitialPositions2",
                        "testBlackStonesInitialDirections",
                        "testBlackStonesInitialCoins"
                    )
                )
                .build()
        ).addChildCriteria(
            Criterion.builder()
                .shortDescription("H2.1 | Aktionen der schwarzen Steine")
                .addChildCriteria(
                    criterion(
                        "In jedem Durchlauf wird ein schwarzer Stein pseudozufällig gewählt.",
                        "testRandomSelection"
                    ),
                    criterion(
                        "Der gewählte Stein ist nicht geschlagen und hat mindestens eine Münze.",
                        "testValidBlackStoneSelection"
                    ),
                    criterion(
                        "Der gewählte Stein legt zum Start des Zugs eine Münze ab.",
                        "testBlackStonePutCoin"
                    ),
                    criterion(
                        "Der gewählte Steine bewegt sich auf das erste Zielfeld, wenn dieses frei ist.",
                        "testFirstTarget"
                    ),
                    criterion(
                        "Der gewählte Roboter bewegt sich bei einem Hindernis auf ein anderes Zielfeld.",
                        "testOtherTargets"
                    ),
                    criterion(
                        "Die Aktionen des schwarzen Steins sind korrekt.",
                        "testRandomSelection",
                        "testValidBlackStoneSelection",
                        "testBlackStonePutCoin",
                        "testFirstTarget",
                        "testOtherTargets"
                    )
                )
                .build()
        ).addChildCriteria(
            Criterion.builder()
                .shortDescription("H2.2 | Aktionen des weißen Steins")
                // 6 Points
                .addChildCriteria(
                    criterion(
                        "Der weiße Stein tut nichts, wenn die Diagonale frei von schwarzen Steinen ist.",
                        "testWhiteStoneNoAction"
                    ),
                    criterion(
                        "Wenn sich auf einem Schlagweg nur ein schwarzer Stein befindet, schlägt der weiße Stein diesen.",
                        "testWhiteStoneHitWithoutTurnedOffRobots"
                    ),
                    criterion(
                        "Wenn sich auf einem Schlagweg bereits geschlagene schwarze Steine befinden, werden diese ignoriert.",
                        "testWhiteStoneHitWithTurnedOffRobots"
                    ),
                    criterion(
                        "Der weiße Stein befindet sich nach einem Schlagzug auf dem letzten Feld des Schlagwegs.",
                        "testWhiteStonePositionAfterHit"
                    ),
                    criterion(
                        "Bei mehreren möglichen Schlagwegen wird einer ausgesucht.",
                        "testWhiteStoneMultipleHits"
                    ),
                    criterion(
                        "Die Aktionen des weißen Steins sind korrekt.",
                        "testWhiteStoneNoAction",
                        "testWhiteStoneHitWithoutTurnedOffRobots",
                        "testWhiteStoneHitWithTurnedOffRobots",
                        "testWhiteStonePositionAfterHit",
                        "testWhiteStoneMultipleHits"
                    )
                )
                .build()
        ).addChildCriteria(
            Criterion.builder()
                .shortDescription("H2.3 | Beendigung der Hauptschleife")
                // 3 Point
                .addChildCriteria(
                    criterion(
                        "Der Fall, dass Team Weiß gewonnen hat, wird korrekt behandelt.",
                        "testTeamWhiteWin"
                    ),
                    criterion(
                        "Der Fall, dass Team Schwarz gewonnen hat, wird korrekt behandelt.",
                        "testTeamBlackWin"
                    ),
                    criterion(
                        "Die Hauptschleife wird korrekt beendet.",
                        "testTeamWhiteWin",
                        "testTeamBlackWin"
                    )
                )
                .build()
        )
        .build();

    private static Criterion criterion(String description, String... methods) {
        var builder = Criterion.builder();
        builder.shortDescription(description);
        if (methods.length > 1) {
            var refs = stream(methods).map(n -> ofMethod(LINK.getMethod(identical(n)).link())).toArray(JUnitTestRef[]::new);
            builder.grader(
                testAwareBuilder()
                    .requirePass(and(refs), "see above")
                    .pointsPassedMax().build()
            );
        } else if (methods.length == 1) {
            builder.grader(
                testAwareBuilder().requirePass(ofMethod(LINK.getMethod(identical(methods[0])).link())).pointsPassedMax()
                    .build());
        }
        return builder.build();
    }

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new AccessTransformer());
    }
}
