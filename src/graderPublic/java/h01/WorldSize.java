package h01;

public record WorldSize(
    int numberOfColumns,
    int numberOfRows
) {

    public int w() {
        return numberOfColumns;
    }

    public int h() {
        return numberOfRows;
    }

    public int maxX() {
        return numberOfColumns - 1;
    }

    public int maxY() {
        return numberOfRows - 1;
    }
}
