package chess.domain.board;

import chess.domain.piece.Empty;
import chess.domain.piece.Piece;

import java.util.HashMap;
import java.util.Map;

public class TestBoardGenerator {

    public static Map<Point, Piece> generate(Map<Point, Piece> pointPieces) {
        Map<Point, Piece> custom = new HashMap<>(pointPieces);
        for (int i = 1; i <= 8; i++) {
            generateLine(custom, i);
        }
        return Map.copyOf(custom);
    }

    private static void generateLine(final Map<Point, Piece> custom, int i) {
        for (int j = 1; j <= 8; j++) {
            Point point = Point.of(i, j);
            custom.computeIfAbsent(point, ignored -> Empty.getInstance());
        }
    }
}
