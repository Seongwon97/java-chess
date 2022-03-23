package chess.game;

import chess.board.Board;
import chess.board.BoardGenerator;

public class Ready extends Started {

    public Ready() {
        super(Board.of(new BoardGenerator()));
    }

    @Override
    public GameState start() {
        return new Running(board);
    }

    @Override
    public GameState finish() {
        return new Finished(board);
    }

    @Override
    public boolean isRunnable() {
        return true;
    }
}
