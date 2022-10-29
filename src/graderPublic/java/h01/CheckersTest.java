package h01;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.Transition;
import fopbot.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.TreeSet;

import static fopbot.Direction.UP;
import static fopbot.RobotFamily.SQUARE_WHITE;
import static fopbot.World.getGlobalWorld;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.rangeClosed;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.items;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.text;

@SuppressWarnings("DuplicatedCode")
@TestForSubmission
public class CheckersTest {

    public static final int N = 250;

    public static TypeLink LINK = BasicTypeLink.of(CheckersTest.class);

    @BeforeAll
    public static void beforeAll() {
        World.setDelay(0);
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h1_1.json")
    public void testWhiteStoneInitialPosition(
        @Property("id") String id,
        @Property("size") WorldSize size
    ) {
        var context = contextBuilder().add("id", id).add(context(size)).build();
        int minX = MAX_VALUE, minY = MAX_VALUE, maxX = MIN_VALUE, maxY = MIN_VALUE;
        int tries = 0;
        while (tries++ < N && (minX != 0 || minY != 0 || maxX != size.maxX() || maxY != size.maxY())) {
            var game = new CheckersStudent(size.numberOfColumns(), size.numberOfRows(), 5, 5);
            call(game::initWhiteStone, context, r -> "initWhiteStone() resulted in an error");
            var stone = assertCallNotNull(game::getWhiteStone, context, r -> "white stone is null after initialization");
            // check if sum of x and y is odd
            if ((stone.getX() + stone.getY()) % 2 != 1) {
                fail(
                    text("odd sum"),
                    text(stone.getX() + stone.getY()),
                    context,
                    r -> "white stone is not always on a field with an odd sum of x- and y-coordinate"
                );
            }
            minX = Math.min(minX, stone.getX());
            minY = Math.min(minY, stone.getY());
            maxX = Math.max(maxX, stone.getX());
            maxY = Math.max(maxY, stone.getY());
        }
        assertEquals(0, minX, context, r -> "white stone cannot reach x-coordinate 0");
        assertEquals(0, minY, context, r -> "white stone cannot reach y-coordinate 0");
        assertEquals(size.maxX(), maxX, context, r -> "white stone cannot reach x-coordinate " + size.maxX());
        assertEquals(size.maxY(), maxY, context, r -> "white stone cannot reach y-coordinate " + size.maxY());
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h1_1.json")
    public void testWhiteStoneInitialDirection(
        @Property("id") String id,
        @Property("size") WorldSize size
    ) {
        var context = contextBuilder().add("id", id).add(context(size)).build();
        var directions = new TreeSet<>();
        int tries = 0;
        while (tries++ < N && directions.size() != Direction.values().length) {
            var game = new CheckersStudent(size.numberOfColumns(), size.numberOfRows(), 5, 5);
            call(game::initWhiteStone, context, r -> "initWhiteStone() resulted in an error");
            var stone = assertCallNotNull(game::getWhiteStone, context, r -> "white stone is null after initialization");
            directions.add(stone.getDirection());
        }
        if (directions.size() != Direction.values().length) {
            fail(
                text("all directions"),
                items("direction", "directions", directions.stream().toList()),
                context,
                r -> "white stone cannot reach each possible directions"
            );
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h1_1.json")
    public void testWhiteStoneInitialCoins(
        @Property("id") String id,
        @Property("size") WorldSize size
    ) {
        var context = contextBuilder().add("id", id).add(context(size)).build();
        int tries = 0;
        var numbers = new TreeSet<>();
        while (tries++ < N) {
            var game = new CheckersStudent(size.numberOfColumns(), size.numberOfRows(), 5, 5);
            call(game::initWhiteStone, context, r -> "initWhiteStone() resulted in an error");
            var stone = assertCallNotNull(game::getWhiteStone, context, r -> "white stone is null after initialization");
            numbers.add(stone.getNumberOfCoins());
        }
        if (!numbers.contains(0) || numbers.size() > 1) {
            fail(
                text("one coin"),
                items(numbers.stream().toList(), "coins", "coins"),
                context,
                r -> "white stone doesn't have correct number of coins"
            );
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h1_2.json")
    public void testBlackStonesInitialPositions1(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("whiteStonePosition") StoneState whiteStonePosition
    ) {
        var context = contextBuilder()
            .add("id", id)
            .add("numberOfColumns", size.numberOfColumns())
            .add("numberOfRows", size.numberOfRows())
            .add("whiteStonePosition", whiteStonePosition)
            .build();
        for (int i = 0; i < 5; i++) {
            int n = i;
            int minX = MAX_VALUE, minY = MAX_VALUE, maxX = MIN_VALUE, maxY = MIN_VALUE;
            int tries = 0;
            while (tries++ < N && (minX != 0 || minY != 0 || maxX != size.maxX() || maxY != size.maxY())) {
                var game = new CheckersStudent(size.numberOfColumns(), size.numberOfRows(), 5, 5);
                game.setWhiteStone(new Robot(whiteStonePosition.x(), whiteStonePosition.y(), UP, 0, SQUARE_WHITE));
                call(game::initBlackStones, context, r -> "initBlackStone() resulted in an error");
                var stones = game.getBlackStones();
                assertCallNotNull(() -> stones[n], context, r -> format("black stone %s is null after initialization", n));
                if ((stones[n].getX() + stones[n].getY()) % 2 != 1) {
                    fail(
                        text("odd sum"),
                        text(stones[n].getX() + stones[n].getY()),
                        context,
                        r -> format("black stone %s is not always on a field with an odd sum of x- and y-coordinate", n)
                    );
                }
                minX = Math.min(minX, stones[n].getX());
                minY = Math.min(minY, stones[n].getY());
                maxX = Math.max(maxX, stones[n].getX());
                maxY = Math.max(maxY, stones[n].getY());
            }
            assertEquals(0, minX, context, r -> format("black stone %s doesn't reach minimum x-coordinate", n));
            assertEquals(0, minY, context, r -> format("black stone %s doesn't reach minimum y-coordinate", n));
            assertEquals(
                size.maxX(),
                maxX,
                context,
                r -> format("black stone %s doesn't reach maximum x-coordinate", n)
            );
            assertEquals(
                size.maxY(),
                maxY,
                context,
                r -> format("black stone %s doesn't reach maximum y-coordinate", n)
            );
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h1_2.json")
    public void testBlackStonesInitialPositions2(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("whiteStonePosition") StoneState whiteStonePosition
    ) {
        var context = contextBuilder()
            .add("id", id)
            .add("numberOfColumns", size.numberOfColumns())
            .add("numberOfRows", size.numberOfRows())
            .add("whiteStonePosition", whiteStonePosition)
            .build();
        for (int i = 0; i < 5; i++) {
            int n = i;
            for (int tries = 0; tries < 20; tries++) {
                var game = new CheckersStudent(size.numberOfColumns(), size.numberOfRows(), 5, 5);
                game.setWhiteStone(new Robot(whiteStonePosition.x(), whiteStonePosition.y(), UP, 0, SQUARE_WHITE));
                call(game::initBlackStones, context, r -> "initBlackStone() resulted in an error");
                var stones = game.getBlackStones();
                assertCallNotNull(() -> stones[n], context, r -> format("black stone %s is null after initialization", n));
                if (stones[n].getX() == whiteStonePosition.x() && stones[n].getY() == whiteStonePosition.y()) {
                    fail(
                        text("different positions"),
                        text(format("same position %s", whiteStonePosition)),
                        context,
                        r -> format("black stone %s can have same position as white stone", n)
                    );
                }
            }
        }
    }

    @Test
    public void testBlackStonesInitialDirections(
    ) {
        var context = emptyContext();
        for (int i = 0; i < 5; i++) {
            int n = i;
            var directions = new TreeSet<>();
            int tries = 0;
            while (tries++ < N && directions.size() != Direction.values().length) {
                var game = new CheckersStudent(5, 5, 5, 5);
                game.setWhiteStone(new Robot(1, 2, UP, 0, SQUARE_WHITE));
                call(game::initBlackStones, context, r -> "initBlackStone() resulted in an error");
                var stones = game.getBlackStones();
                assertCallNotNull(() -> stones[n], context, r -> format("black stone %s is null after initialization", n));
                directions.add(stones[n].getDirection());
            }
            if (directions.size() != Direction.values().length) {
                fail(
                    text("all directions"),
                    text(directions),
                    context,
                    r -> format("black stone %s cannot reach each possible directions", n)
                );
            }
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h1_2.json")
    public void testBlackStonesInitialCoins(
        @Property("id") String id,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins
    ) {
        var context = contextBuilder()
            .add("id", id)
            .add(context(numberOfCoins))
            .build();
        for (int i = 0; i < 5; i++) {
            int n = i;
            var expectedCoinNumbers = rangeClosed(numberOfCoins.minNumberOfCoins(), numberOfCoins.max()).boxed().collect(toSet());
            var coinNumbers = new TreeSet<>();
            int tries = 0;
            while (tries++ < 200 && coinNumbers.size() != expectedCoinNumbers.size()) {
                var game = new CheckersStudent(5, 5, numberOfCoins.min(), numberOfCoins.max());
                game.setWhiteStone(new Robot(1, 2, UP, 0, SQUARE_WHITE));
                call(game::initBlackStones, context, r -> "initBlackStone() resulted in an error");
                var stone = game.getBlackStones()[i];
                assertCallNotNull(() -> stone, context, r -> format("black stone %s is null after initialization", n));
                coinNumbers.add(stone.getNumberOfCoins());
            }
            assertEquals(
                expectedCoinNumbers,
                coinNumbers,
                context,
                r -> format("black stone %s is not initialized with expected numbers of coins", n)
            );
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_1_1.json")
    public void testRandomSelection(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore
    ) {
        var context = contextBuilder()
            .add("id", id)
            .add(context(size))
            .add(context(statesBefore))
            .build();
        var selectedRobots = new TreeSet<Integer>();
        for (int i = 0; i < N; i++) {
            var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
            game.init(statesBefore);
            var tracker = game.blackStoneTracker(context);
            call(game::doBlackTeamActions, context, r -> "doBlackTeamActions() resulted in an error");
            selectedRobots.add(tracker.get());
        }
        if (selectedRobots.size() < 5) {
            fail(
                text("all robots"),
                text(selectedRobots),
                context,
                r -> "not all robots were selected"
            );
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_1_1.json")
    public void testValidBlackStoneSelection(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore
    ) {
        var context = contextBuilder()
            .add("id", id)
            .add(context(size))
            .add(context(statesBefore))
            .build();
        for (int i = 0; i < N; i++) {
            var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
            game.init(statesBefore);
            var tracker = game.blackStoneTracker(context);
            call(game::doBlackTeamActions, context, r -> "doBlackTeamActions() resulted in an error");
            var blackStoneNumber = tracker.get();
            var state = statesBefore.blackStone(blackStoneNumber);
            if (state.numberOfCoins() == 0) {
                assertEquals(
                    text("black stone with coins"),
                    text("black stone without coins"),
                    context,
                    r -> format("black stone %s was selected but it had no coins", blackStoneNumber)
                );
            }
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_1_2.json")
    public void testBlackStonePutCoin(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore
    ) {
        var context = contextBuilder()
            .add("id", id)
            .add(context(size))
            .add(context(statesBefore))
            .build();
        for (int i = 0; i < N; i++) {
            var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
            game.init(statesBefore);
            var tracker = game.blackStoneTracker(context);
            call(game::doBlackTeamActions, context, r -> "doBlackTeamActions() resulted in an error");
            var blackStoneNumber = tracker.get();
            var blackStone = game.getBlackStones()[blackStoneNumber];
            var robotTrace = getGlobalWorld().getTrace(blackStone);
            if (robotTrace.getTransitions().get(0).action != Transition.RobotAction.PUT_COIN) {
                fail(
                    text("put coin"),
                    text("no put coin"),
                    context,
                    r -> format("black stone %s was selected but it didn't put a coin as the first action", blackStoneNumber)
                );
            }
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_1_3.json")
    public void testFirstTarget(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore,
        @Property("statesAfter") StoneStates statesAfter
    ) {
        testTargets(id, size, numberOfCoins, statesBefore, statesAfter);
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_1_4.json")
    public void testOtherTargets(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore,
        @Property("statesAfter") StoneStates statesAfter
    ) {
        testTargets(id, size, numberOfCoins, statesBefore, statesAfter);
    }

    public void testTargets(
        String id,
        WorldSize size,
        NumberOfCoins numberOfCoins,
        StoneStates statesBefore,
        StoneStates statesAfter
    ) {
        var context = contextBuilder()
            .add("id", id)
            .add(context(size))
            .add(context(statesBefore))
            .build();
        for (int i = 0; i < N; i++) {
            var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
            game.init(statesBefore);
            var tracker = game.blackStoneTracker(context);
            call(game::doBlackTeamActions, context, r -> "doBlackTeamActions() resulted in an error");
            var blackStoneNumber = tracker.get();
            var blackStone = game.getBlackStones()[blackStoneNumber];
            var expected = statesAfter.blackStone(blackStoneNumber).toPositionOnly();
            var actual = StoneState.of(blackStone).toPositionOnly();
            assertEquals(
                expected,
                actual,
                context,
                r -> format("black stone %s was not moved to the expected position", blackStoneNumber)
            );
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_2_1.json")
    public void testWhiteStoneNoAction(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore
    ) {
        var context = contextBuilder()
            .add("id", id)
            .add(context(size))
            .add(context(statesBefore))
            .build();
        for (int i = 0; i < N; i++) {
            var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
            game.init(statesBefore);
            call(game::doWhiteTeamActions, context, r -> "doBlackTeamActions() resulted in an error");
            var stoneStates = game.getStoneStates();
            var turnedOffBlackStones = rangeClosed(0, 4).filter(n -> stoneStates.blackStone(n).turnedOff()).boxed().toList();
            if (!turnedOffBlackStones.isEmpty()) {
                fail(
                    text("no turned off black stones"),
                    items("robot", "robots", turnedOffBlackStones, "was turned off", "were turned off"),
                    context,
                    r -> "black robots were turned off"
                );
            }
            var transitions = game.getTrace(game.getWhiteStone()).getTransitions().stream().map(t -> t.action).collect(toList());
            transitions.remove(0);
            if (!transitions.isEmpty()) {
                fail(
                    text("no actions"),
                    items("transition", "transitions", transitions),
                    context,
                    r -> "white stone performed actions"
                );
            }
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_2_2.json")
    public void testWhiteStoneHitWithoutTurnedOffRobots(
        @Property("id") String test,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore,
        @Property("blackStoneToTurnOff") int blackStoneToTurnOff
    ) {
        testWhiteStoneOneHit(test, size, numberOfCoins, statesBefore, blackStoneToTurnOff, 1);
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_2_3.json")
    public void testWhiteStoneHitWithTurnedOffRobots(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore,
        @Property("blackStoneToTurnOff") int blackStoneToTurnOff
    ) {
        testWhiteStoneOneHit(id, size, numberOfCoins, statesBefore, blackStoneToTurnOff, 2);
    }

    private void testWhiteStoneOneHit(
        String test,
        WorldSize size,
        NumberOfCoins numberOfCoins,
        StoneStates statesBefore,
        int blackStoneToTurnOff,
        int numberOfTurnedOffBlackStones
    ) {
        var context = contextBuilder().add("id", test).add(context(size, statesBefore)).build();
        for (int i = 0; i < N; i++) {
            var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
            game.init(statesBefore);
            call(game::doWhiteTeamActions, context, r -> "doWhiteTeamActions() resulted in an error");
            var stoneStates = game.getStoneStates();
            var turnedOffBlackStones = rangeClosed(0, 4).filter(n -> stoneStates.blackStone(n).turnedOff()).boxed().toList();
            if (!turnedOffBlackStones.contains(blackStoneToTurnOff)) {
                fail(
                    text("black stone " + blackStoneToTurnOff + " is turned off"),
                    items("black stone", "black stones", turnedOffBlackStones, "is turned off", "are turned off"),
                    context,
                    r -> format("black stone %s was not turned off", blackStoneToTurnOff)
                );
            }
            if (turnedOffBlackStones.size() != numberOfTurnedOffBlackStones) {
                fail(
                    text("black stone " + blackStoneToTurnOff + " is turned off"),
                    items("robot", "robots", turnedOffBlackStones, "was turned off", "were turned off"),
                    context,
                    r -> "more than one black stone was turned off"
                );
            }
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_2_2.json")
    public void testWhiteStonePositionAfterHit(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore,
        @Property("statesAfter") StoneStates statesAfter
    ) {
        var context = contextBuilder().add("id", id).add(context(size, statesBefore)).build();
        var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
        game.init(statesBefore);
        call(game::doWhiteTeamActions, context, r -> "doWhiteTeamActions() resulted in an error");
        assertEquals(
            statesAfter.whiteStone().toPositionOnly(),
            game.getStoneStates().whiteStone().toPositionOnly(),
            context,
            r -> "white stone has not moved to correct position"
        );
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_2_4.json")
    public void testWhiteStoneMultipleHits(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore
    ) {
        var context = contextBuilder().add("id", id).add(context(size, statesBefore)).build();
        var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
        game.init(statesBefore);
        call(game::doWhiteTeamActions, context, r -> "doWhiteTeamActions() resulted in an error");
        var stoneStates = game.getStoneStates();
        var turnedOffBlackStones = rangeClosed(0, 4).filter(n -> stoneStates.blackStone(n).turnedOff()).boxed().toList();
        if (turnedOffBlackStones.size() == 0) {
            fail(
                text("one turned off black stone"),
                text("no turned off black stones"),
                context,
                r -> "no black stone was turned off"
            );
        }
        if (turnedOffBlackStones.size() > 1) {
            fail(
                text("one turned off black stone"),
                items("black stone", "black stones", turnedOffBlackStones, "was turned off", "were turned off"),
                context,
                r -> "more than one black stone was turned off"
            );
        }
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_3_1.json")
    public void testTeamWhiteWin(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore
    ) {
        var context = contextBuilder().add("id", id).add(context(size, statesBefore)).build();
        var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
        game.init(statesBefore);
        call(game::updateGameState, context, r -> "updateGameState() resulted in an error");
        assertEquals(
            GameState.WHITE_WIN,
            game.getGameState(),
            context,
            r -> "unexpected game state"
        );
    }

    @ParameterizedTest
    @JsonClasspathSource("h01/h2_3_2.json")
    public void testTeamBlackWin(
        @Property("id") String id,
        @Property("size") WorldSize size,
        @Property("numberOfCoins") NumberOfCoins numberOfCoins,
        @Property("statesBefore") StoneStates statesBefore
    ) {
        var context = contextBuilder().add("id", id).add(context(size, statesBefore)).build();
        var game = new CheckersStudent(size.w(), size.h(), numberOfCoins.min(), numberOfCoins.max());
        game.init(statesBefore);
        call(game::updateGameState, context, r -> "updateGameState resulted in an error");
        assertEquals(
            GameState.BLACK_WIN,
            game.getGameState(),
            context,
            r -> "unexpected game state"
        );
    }


}
