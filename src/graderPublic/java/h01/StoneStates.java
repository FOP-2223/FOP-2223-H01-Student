package h01;

@SuppressWarnings("DuplicatedCode")
public record StoneStates(
    StoneState whiteStone,
    StoneState blackStone0,
    StoneState blackStone1,
    StoneState blackStone2,
    StoneState blackStone3,
    StoneState blackStone4
) {

    public static StoneStates of(CheckersStudent game) {
        return new StoneStates(
            StoneState.of(game.getWhiteStone()),
            StoneState.of(game.getBlackStones()[0]),
            StoneState.of(game.getBlackStones()[1]),
            StoneState.of(game.getBlackStones()[2]),
            StoneState.of(game.getBlackStones()[3]),
            StoneState.of(game.getBlackStones()[4])
        );
    }

    StoneState blackStone(int n) {
        return switch (n) {
            case 0 -> blackStone0;
            case 1 -> blackStone1;
            case 2 -> blackStone2;
            case 3 -> blackStone3;
            case 4 -> blackStone4;
            default -> null;
        };
    }
}
