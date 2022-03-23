package chess.domain.piece;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }
}
