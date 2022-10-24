package h01;

import fopbot.Direction;
import fopbot.Robot;

public record StoneState(
    int x,
    int y,
    Direction direction,
    int numberOfCoins,
    boolean turnedOn
) {

    public static StoneState of(Robot robot) {
        return new StoneState(robot.getX(), robot.getY(), robot.getDirection(), robot.getNumberOfCoins(), robot.isTurnedOn());
    }

    public boolean turnedOff() {
        return !turnedOn;
    }

    public WithoutDirection toWithoutDirection() {
        return new WithoutDirection(x, y, numberOfCoins, turnedOn);
    }

    public PositionOnly toPositionOnly() {
        return new PositionOnly(x, y);
    }

    @Override
    public String toString() {
        return "{" +
            "x=" + x +
            ", y=" + y +
            ", numberOfCoins=" + numberOfCoins +
            ", direction=" + direction +
            ", turnedOn=" + turnedOn +
            '}';
    }

    public record WithoutDirection(
        int x,
        int y,
        int numberOfCoins,
        boolean turnedOn
    ) {
        public static WithoutDirection of(Robot robot) {
            return new WithoutDirection(robot.getX(), robot.getY(), robot.getNumberOfCoins(), robot.isTurnedOn());
        }

        @Override
        public String toString() {
            return "{" +
                "x = " + x +
                ", y = " + y +
                ", numberOfCoins = " + numberOfCoins +
                ", turnedOn = " + turnedOn +
                '}';
        }
    }

    public record PositionOnly(
        int x,
        int y
    ) {
        public static PositionOnly of(Robot robot) {
            return new PositionOnly(robot.getX(), robot.getY());
        }

        @Override
        public String toString() {
            return "{" +
                "x = " + x +
                ", y = " + y +
                '}';
        }
    }
}
