package dev.lqwd.controllers;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.match_score.MatchScoreForUpdateRequestDto;
import dev.lqwd.dto.match_score.MatchScoreUpdatedResponseDto;
import dev.lqwd.dto.match_score.MatchScoreWinnerResponseDto;
import dev.lqwd.exception.NotFoundException;
import dev.lqwd.service.FinishedMatchesPersistenceService;
import dev.lqwd.service.MatchScoreCalculationService;
import dev.lqwd.service.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebServlet("/match-score")
public class MatchScoreController extends BasicServlet {

    private static final String MATCH_SCORE_URL = "match-score?uuid=%s";
    private static final String NOT_FOUND_NAME_ERROR_MESSAGE = "The player with id: %s not found";
    private static final MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
    private static final PlayerDao playerDao = new PlayerDao();
    private static final FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService();
    private static final int PLAYER_1_NUMBER = 1;
    private static final int PLAYER_2_NUMBER = 2;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UUID uuid = UUID.fromString(
                req.getParameter("uuid"));

        log.info("uuid is redirected: {}}", uuid);

        MatchScoreDto matchScoreDto = OngoingMatchesService.getInstance().getMatchScoreDto(uuid);

        req.setAttribute("matchScore", setUpdatedMatchScore(matchScoreDto));
        req.getRequestDispatcher("match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UUID uuid = UUID.fromString(
                req.getParameter("uuid"));

        int pointWinnerNumber = Integer.parseInt(
                req.getParameter("playerNumber"));

        log.info("point for player number: {}}", pointWinnerNumber);

        MatchScoreForUpdateRequestDto matchScoreForUpdateRequestDto = MatchScoreForUpdateRequestDto.builder()
                .matchScoreDto(OngoingMatchesService.getInstance().getMatchScoreDto(uuid).toBuilder().build())
                .pointWinnerNumber(pointWinnerNumber)
                .build();

        MatchScoreDto matchScoreDtoUpdated = matchScoreCalculationService.calculateScore(matchScoreForUpdateRequestDto);

        if (isWinner(matchScoreDtoUpdated)) {

            OngoingMatchesService.getInstance().removeMatch(uuid);
            finishedMatchesPersistenceService.saveMatch(matchScoreDtoUpdated);

            log.trace("matched removed and saved: {}}", matchScoreDtoUpdated);

            req.setAttribute("matchScore", setWinnerMatchScore(matchScoreDtoUpdated));
            req.getRequestDispatcher("match-winner.jsp").forward(req, resp);

        } else {

            OngoingMatchesService.getInstance().updateScore(uuid, matchScoreDtoUpdated);

            log.info("score updated for uuid: {}, score: {} }", uuid, matchScoreDtoUpdated);

            resp.sendRedirect(MATCH_SCORE_URL.formatted(uuid));
        }

    }

    private static boolean isWinner(MatchScoreDto matchScoreDtoUpdated) {

        return matchScoreDtoUpdated.getIdWinner() != null;

    }

    private static MatchScoreUpdatedResponseDto setUpdatedMatchScore(MatchScoreDto matchScoreDtoForUpdate) {

        long player1Id = matchScoreDtoForUpdate.getIdPlayer1();
        long player2Id = matchScoreDtoForUpdate.getIdPlayer2();

        return MatchScoreUpdatedResponseDto.builder()
                .matchScoreDto(matchScoreDtoForUpdate)
                .numberPlayer1(PLAYER_1_NUMBER)
                .numberPlayer2(PLAYER_2_NUMBER)
                .player1Name(playerDao.findById(player1Id)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE.formatted(player1Id)))
                        .getName())
                .player2Name(playerDao.findById(player2Id)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE.formatted(player2Id)))
                        .getName())
                .build();

    }

    private static MatchScoreWinnerResponseDto setWinnerMatchScore(MatchScoreDto matchScoreDtoUpdated) {

        long player1Id = matchScoreDtoUpdated.getIdPlayer1();
        long player2Id = matchScoreDtoUpdated.getIdPlayer2();
        long winnerId = matchScoreDtoUpdated.getIdWinner();

        return MatchScoreWinnerResponseDto.builder()
                .matchScoreDto(matchScoreDtoUpdated)
                .player1Name(playerDao.findById(player1Id)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE.formatted(player1Id)))
                        .getName())
                .player2Name(playerDao.findById(player2Id)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE.formatted(player2Id)))
                        .getName())
                .winnerName(playerDao.findById(winnerId)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE.formatted(winnerId)))
                        .getName())
                .build();

    }

}