package web.service;

import chess.domain.board.*;
import chess.domain.game.Game;
import chess.domain.piece.*;
import web.dao.PieceDao;
import web.dao.GameDao;
import web.dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessService {

    private final PieceDao boardDao;
    private final GameDao gameDao;

    public ChessService(PieceDao boardDao, GameDao gameDao) {
        this.boardDao = boardDao;
        this.gameDao = gameDao;
    }

    public WebBoardDto startNewGame(GameInfoDto gameDto) {
        gameDao.save(gameDto);
        Game game = new Game(new Board(InitialBoardGenerator.generate()), Color.WHITE);

        Map<Point, Piece> pointPieces = game.getPointPieces();
        for (Point point : pointPieces.keySet()) {
            savePiece(gameDto, pointPieces, point);
        }
        return new WebBoardDto(game.getPointPieces(), game.getTurnColor(), game.isRunnable());
    }

    private void savePiece(GameInfoDto gameDto, Map<Point, Piece> responseBoard, Point point) {
        if (!responseBoard.get(point).isSameType(PieceType.EMPTY)) {
            boardDao.save(new PieceDto(gameDto.getRoomName(), point.convertPointToId(),
                    responseBoard.get(point).getPieceType(), responseBoard.get(point).getPieceColor()));
        }
    }

    public WebBoardDto resumeGame(GameInfoDto gameDto) {
        Game game = findGame(gameDto);
        return new WebBoardDto(game.getPointPieces(), game.getTurnColor(), game.isRunnable());
    }

    public Game findGame(GameDto gameDto) {
        GameInfoDto findGame = gameDao.findByRoomName(gameDto.getRoomName());
        if (findGame == null) {
            throw new IllegalArgumentException("[ERROR] 존재하는 게임이 없습니다.");
        }
        List<PieceDto> allPieces = boardDao.findAllPiecesByRoomName(gameDto.getRoomName());
        Map<Point, Piece> pointPieces = new HashMap<>(createBoard(allPieces));

        return new Game(new Board(pointPieces), Color.convertColorByString(findGame.getTurnColor()));
    }

    public Map<Point, Piece> createBoard(List<PieceDto> allPieces) {
        Map<Point, Piece> pointPieces = new HashMap<>();
        for (PieceDto piece : allPieces) {
            pointPieces.put(Point.of(piece.getPosition()), PieceConverter.convert(piece.getPieceType(), piece.getPieceColor()));
        }
        return BoardGenerator.generate(pointPieces);
    }

    public WebBoardDto move(MoveInfoDto moveInfo) {
        Game game = findGame(moveInfo);
        game.move(List.of(Point.of(moveInfo.getFrom()), Point.of(moveInfo.getTo())));
        boardDao.deleteByRoomNameAndPosition(moveInfo.getRoomName(), moveInfo.getTo());
        boardDao.update(moveInfo.getRoomName(), moveInfo.getFrom(), moveInfo.getTo());
        gameDao.update(new GameInfoDto(moveInfo.getRoomName(), game.getTurnColor()));
        return new WebBoardDto(game.getPointPieces(), game.getTurnColor(), game.isRunnable());
    }

    public WebStatusDto status(GameInfoDto gameInfoDto) {
        Game game = findGame(gameInfoDto);
        return new WebStatusDto(game.calculateScore().get(Color.WHITE),
                game.calculateScore().get(Color.BLACK));
    }

    public void deleteAndFinish(GameInfoDto gameDto) {
        gameDao.delete(gameDto.getRoomName());
    }
}
