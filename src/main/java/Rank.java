import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Rank {
    ONE(1), TWO(2), THREE(3), FOUR(4),
    FIVE(5), SIX(6), SEVEN(7), EIGHT(8), INVALID(0);

    private int value;

    Rank(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Rank find(int value){
        return Arrays.stream(Rank.values())
                        .filter(r -> r.value == value)
                        .findFirst()
                        .orElse(INVALID);
    }

    public static List<Rank> list(){
        return Arrays.stream(Rank.values())
                        .filter(r -> r != Rank.INVALID)
                        .collect(Collectors.toList());
    }
}
