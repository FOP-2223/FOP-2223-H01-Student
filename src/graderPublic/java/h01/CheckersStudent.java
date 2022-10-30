package h01;

import fopbot.Robot;
import fopbot.RobotFamily;
import fopbot.RobotTrace;
import fopbot.World;
import org.tudalgo.algoutils.student.io.PropertyUtils;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static fopbot.RobotFamily.SQUARE_WHITE;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.text;
import static org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers.identical;

public class CheckersStudent {

    private static final Properties properties;
    private static final TypeLink type;
    private static final FieldLink whiteStoneLink;
    private static final FieldLink[] blackStoneLinks;
    private static FieldLink blackStoneArrayLink;
    private static final FieldLink NUMBER_OF_ROWS;
    private static final FieldLink NUMBER_OF_COLUMNS;
    private static final FieldLink MIN_NUMBER_OF_COINS;
    private static final FieldLink MAX_NUMBER_OF_COINS;
    private static final FieldLink GAME_STATE_LINK;

    static {
        properties = PropertyUtils.getProperties("checkers.properties");
        type = BasicTypeLink.of(Checkers.class);
        whiteStoneLink = type.getField(identical("whiteStone"));
        blackStoneLinks = new FieldLink[]{
            type.getField(identical("blackStone0")),
            type.getField(identical("blackStone1")),
            type.getField(identical("blackStone2")),
            type.getField(identical("blackStone3")),
            type.getField(identical("blackStone4"))
        };
        blackStoneArrayLink = type.getField(Matcher.of(l -> l.link().getType() == Robot[].class));
        if (blackStoneArrayLink.link().getType() != Robot[].class) {
            blackStoneArrayLink = null;
        }
        NUMBER_OF_ROWS = type.getField(identical("NUMBER_OF_ROWS"));
        NUMBER_OF_COLUMNS = type.getField(identical("NUMBER_OF_COLUMNS"));
        MIN_NUMBER_OF_COINS = type.getField(identical("MIN_NUMBER_OF_COINS"));
        MAX_NUMBER_OF_COINS = type.getField(identical("MAX_NUMBER_OF_COINS"));
        GAME_STATE_LINK = type.getField(identical("gameState"));

    }

    private final Checkers instance;

    public CheckersStudent(int numberOfColumns, int numberOfRows, int minNumberOfCoins, int maxNumberOfCoins) {
        this.instance = new Checkers();
        setConfiguration(numberOfColumns, numberOfRows, minNumberOfCoins, maxNumberOfCoins);
    }

    public void initWhiteStone() {
        instance.initWhiteStone();
    }

    public void initBlackStones() {
        instance.initBlackStones();
    }

    public void init(StoneStates states) {
        var whiteStoneState = states.whiteStone();
        var whiteStone = new Robot(
            whiteStoneState.x(),
            whiteStoneState.y(),
            whiteStoneState.direction(),
            whiteStoneState.numberOfCoins(),
            SQUARE_WHITE
        );
        setWhiteStone(whiteStone);
        for (int i = 0; i < 5; i++) {
            var blackStoneState = states.blackStone(i);
            var blackStone = new Robot(
                blackStoneState.x(),
                blackStoneState.y(),
                blackStoneState.direction(),
                blackStoneState.numberOfCoins(),
                RobotFamily.SQUARE_BLACK
            );
            if (blackStoneState.numberOfCoins() == 0) {
                World.putCoins(blackStoneState.x(), blackStoneState.y(), 1);
            }
            if (blackStoneState.turnedOff()) {
                blackStone.turnOff();
            }
            setBlackStone(i, blackStone);
        }
    }

    public Robot getWhiteStone() {
        return whiteStoneLink.get(instance);
    }

    public void setWhiteStone(Robot robot) {
        whiteStoneLink.set(instance, robot);
    }

    public void setBlackStone(int i, Robot robot) {
        if (blackStoneArrayLink != null) {
            var array = blackStoneArrayLink.get(instance);
            if (array == null) {
                array = new Robot[5];
                blackStoneArrayLink.set(instance, array);
            }
            ((Robot[]) array)[i] = robot;
        } else {
            blackStoneLinks[i].set(instance, robot);
        }
    }

    public Robot[] getBlackStones() {
        if (blackStoneArrayLink != null) {
            return blackStoneArrayLink.get(instance);
        } else {
            return stream(blackStoneLinks).map(blackStoneLink -> blackStoneLink.get(instance)).toArray(Robot[]::new);
        }
    }

    public StoneStates getStoneStates() {
        return StoneStates.of(this);
    }

    public void doBlackTeamActions() {
        instance.doBlackTeamActions();
    }

    public void doWhiteTeamActions() {
        instance.doWhiteTeamActions();
    }

    public void updateGameState() {
        instance.updateGameState();
    }

    public GameState getGameState() {
        return GAME_STATE_LINK.get(instance);
    }

    public List<StoneState> blackStoneStates() {
        return Arrays.stream(getBlackStones()).map(StoneState::of).collect(Collectors.toUnmodifiableList());
    }

    public Supplier<Integer> blackStoneTracker(Context context) {
        var blackStonePositions = blackStoneStates();
        return () -> {
            var usedBlackStones = new LinkedList<Integer>();
            var newBlackStonePositions = blackStoneStates();
            for (int i = 0; i < 5; i++) {
                if (!blackStonePositions.get(i).equals(newBlackStonePositions.get(i))) {
                    usedBlackStones.add(i);
                }
            }
            if (usedBlackStones.isEmpty()) {
                fail(
                    text("one black stone"),
                    text("no black stone"),
                    context,
                    r -> "no black stone made an action"
                );
            } else if (usedBlackStones.size() != 1) {
                fail(
                    text("one black stone"),
                    text(format("%s black stones", usedBlackStones.size())),
                    context,
                    r -> "more than one black stone made an action"
                );
            }
            return usedBlackStones.get(0);
        };
    }

    public RobotTrace getTrace(Robot robot) {
        return World.getGlobalWorld().getTrace(robot);
    }

    public void setConfiguration(int numberOfColumns, int numberOfRows, int minNumberOfCoins, int maxNumberOfCoins) {
        // set properties
        properties.put("NUMBER_OF_COLUMNS", numberOfColumns);
        properties.put("NUMBER_OF_ROWS", numberOfRows);
        properties.put("MIN_NUMBER_OF_COINS", minNumberOfCoins);
        properties.put("MAX_NUMBER_OF_COINS", maxNumberOfCoins);
        // set fields
        NUMBER_OF_COLUMNS.set(instance, numberOfColumns);
        NUMBER_OF_ROWS.set(instance, numberOfRows);
        MIN_NUMBER_OF_COINS.set(instance, minNumberOfCoins);
        MAX_NUMBER_OF_COINS.set(instance, maxNumberOfCoins);
        World.setSize(numberOfColumns, numberOfRows);
    }
}
