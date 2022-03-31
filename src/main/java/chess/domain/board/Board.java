package chess.domain.board;

import chess.domain.piece.Color;
import chess.domain.piece.Empty;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Board {

    private final Map<Point, Piece> pointPieces;

    public Board(Map<Point, Piece> pointPieces) {
        this.pointPieces = new HashMap<>(pointPieces);
    }

    public void move(Point from, Point to, Color turnColor) {
        Piece fromPiece = pointPieces.get(from);
        Piece toPiece = pointPieces.get(to);
        validateFromAndToPlace(turnColor, fromPiece, toPiece);
        movePiece(from, to, fromPiece);
    }

    private void validateFromAndToPlace(Color turnColor, Piece fromPiece, Piece toPiece) {
        validateAllyMove(turnColor, fromPiece);
        validateNotAllyAttack(turnColor, toPiece);
    }

    private void validateAllyMove(Color turnColor, Piece fromPiece) {
        if (!fromPiece.isSameColor(turnColor)) {
            throw new IllegalArgumentException("[ERROR] 자신의 말을 움직여야 합니다.");
        }
    }

    private void validateNotAllyAttack(Color turnColor, Piece toPiece) {
        if (toPiece.isSameColor(turnColor)) {
            throw new IllegalArgumentException("[ERROR] 아군을 공격할 수 없습니다.");
        }
    }

    private void movePiece(Point from, Point to, Piece fromPiece) {
        Map<Point, Piece> copyOfPointPieces = Map.copyOf(pointPieces);
        fromPiece.move(copyOfPointPieces, from, to);
        pointPieces.put(to, fromPiece);
        pointPieces.put(from, Empty.getInstance());
    }

    public boolean isKingDead(Color turnColor) {
        return pointPieces.values().stream()
                .noneMatch(piece -> piece.isSameType(PieceType.KING) &&
                        piece.isSameColor(turnColor));
    }

    public Map<Color, Double> calculateScore() {
        Map<Color, Double> map = new LinkedHashMap<>();
        map.put(Color.WHITE, PieceType.calculateScore(pointPieces, Color.WHITE));
        map.put(Color.BLACK, PieceType.calculateScore(pointPieces, Color.BLACK));
        return map;
    }

    public Map<Point, Piece> getPointPieces() {
        return Map.copyOf(pointPieces);
    }
}
