package geneticisst.checkerss;

public class Moves {
    public enum MoveType {
        NONE, STEP, JUMP;
    }
    public MoveType type;
    public Shashka shashka;

    public Moves(MoveType type, Shashka shashka) {
        this.type = type;
        this.shashka = shashka;
    }

    public Moves(MoveType type) {
        this(type, null);
    }

    public static MoveType currentMove(Tile[][] field, int x, int y) {
        return MoveType.NONE;
    }
}
