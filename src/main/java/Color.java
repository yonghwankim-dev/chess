public enum Color {
    BLACK("B"), WHITE("W"), EMPTY("E");

    private String shortName;

    Color(String shortName) {
        this.shortName = shortName;
    }

    public String shortName() {
        return shortName;
    }
}
