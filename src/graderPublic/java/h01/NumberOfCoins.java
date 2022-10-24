package h01;

public record NumberOfCoins(
    int minNumberOfCoins,
    int maxNumberOfCoins
) {

    public int min() {
        return minNumberOfCoins;
    }

    public int max() {
        return maxNumberOfCoins;
    }
}
