package bg.fmi.intelligent.systems;

public enum Moves {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    NEUTRAL;

    public Moves getTheOpposite() {
        Moves opposite = NEUTRAL;
        switch (this) {
            case RIGHT:
                opposite = Moves.LEFT;
                break;
            case LEFT:
                opposite = Moves.RIGHT;
                break;
            case UP:
                opposite = Moves.DOWN;
                break;
            case DOWN:
                opposite = Moves.UP;
                break;
        }
        return opposite;
    }

}
