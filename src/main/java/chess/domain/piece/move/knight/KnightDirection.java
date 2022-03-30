package chess.domain.piece.move.knight;

import chess.domain.board.Point;
import chess.domain.piece.move.Direction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum KnightDirection implements Direction {

    NNW(-1, 2),
    NNE(1, 2),
    WWN(-2, 1),
    EEN(2, 1),
    SSW(-1, -2),
    SSE(1, -2),
    WWS(-2, -1),
    EES(2, -1);

    private final int dx;
    private final int dy;

    KnightDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static List<Point> createNextPointCandidates(Point from) {
        return Arrays.stream(values())
                .filter(value -> from.isInRangeNext(value.dx, value.dy))
                .map(value -> value.nextOf(from))
                .collect(Collectors.toList());
    }

    @Override
    public Point nextOf(Point point) {
        return Point.of(point.moveHorizontal(dx), point.moveVertical(dy));
    }
}
