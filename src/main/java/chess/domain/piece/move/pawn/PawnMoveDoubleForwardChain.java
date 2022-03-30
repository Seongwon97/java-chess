package chess.domain.piece.move.pawn;

import chess.domain.board.Point;
import chess.domain.piece.Piece;
import chess.domain.piece.move.StraightDirection;

import java.util.Map;

public class PawnMoveDoubleForwardChain extends PawnMoveChain {

    private final PawnMoveChain next;

    public PawnMoveDoubleForwardChain(PawnSupport support) {
        super(support);
        this.next = new PawnMoveDiagonalChain(support);
    }

    @Override
    public void move(Map<Point, Piece> pointPieces, Point from, Point to) {
        int horizontal = to.subtractHorizontal(from);
        int vertical = support.forwarding(to.subtractVertical(from));
        StraightDirection direction = StraightDirection.find(from, to);
        Point middle = direction.nextOf(from);
        if (isStartLine(from) &&
                isToPoint(horizontal, vertical) &&
                isNoObstacle(pointPieces, to, middle)) {
            return;
        }
        next.move(pointPieces, from, to);
    }

    private boolean isStartLine(Point from) {
        return support.isStartLine(from);
    }

    private boolean isNoObstacle(Map<Point, Piece> pointPieces, Point to, Point middle) {
        return isEmptyPoint(pointPieces, to) && isEmptyPoint(pointPieces, middle);
    }

    private boolean isToPoint(int horizontal, int vertical) {
        return vertical == 2 && horizontal == 0;
    }
}
