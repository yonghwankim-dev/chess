import java.util.Arrays;

public enum File {
    A(1),B(2),C(3),D(4),E(5),F(6),G(7),H(8), INVALID(0);

    private int value;

    File(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static File find(int value) {
        return Arrays.stream(File.values()).filter(f->f.value == value)
                    .findFirst()
                    .orElse(INVALID);

    }
}
