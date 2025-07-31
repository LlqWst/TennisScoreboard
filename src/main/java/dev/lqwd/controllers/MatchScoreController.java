package dev.lqwd.controllers;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.dto.MatchScoreDto;
import dev.lqwd.dto.RequestForScoreUpdateDto;
import dev.lqwd.dto.UpdatedScoreDto;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UUID uuid = UUID.fromString(
                req.getParameter("uuid"));

        log.info("uuid is redirected: {}}", uuid);

        setMatchAttributes(req, uuid);

        req.getRequestDispatcher("match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UUID uuid = UUID.fromString(
                req.getParameter("uuid"));

        int pointWinnerNumber = Integer.parseInt(
                req.getParameter("playerNumber"));

        log.info("point for player number: {}}", pointWinnerNumber);

        RequestForScoreUpdateDto updatingScoreDto = RequestForScoreUpdateDto.builder()
                .matchScoreDto(OngoingMatchesService.getInstance().getMatch(uuid).toBuilder().build())
                .pointWinnerNumber(pointWinnerNumber)
                .build();

        UpdatedScoreDto updatedScoreDto = matchScoreCalculationService.updateScore(updatingScoreDto);

        if (updatedScoreDto.getIsWinner()) {

            OngoingMatchesService.getInstance().removeMatch(uuid);
            finishedMatchesPersistenceService.saveMatch(updatedScoreDto);

            setWinnerAttributes(req, updatedScoreDto);
            req.getRequestDispatcher("match-score_winner.jsp").forward(req, resp);

        } else {

            OngoingMatchesService.getInstance().updateScore(uuid, updatedScoreDto.getUpdatedScore());

            log.info("score updated for uuid: {}, score: {} }", uuid, updatedScoreDto);

            resp.sendRedirect(MATCH_SCORE_URL.formatted(uuid));
        }

    }

    private void setMatchAttributes(HttpServletRequest req, UUID uuid) {

        req.setAttribute("match", OngoingMatchesService.getInstance().getMatch(uuid));

        req.setAttribute("numberPlayer1", 1);

        req.setAttribute("numberPlayer2", 2);

        MatchScoreDto matchScoreDto = OngoingMatchesService.getInstance().getMatch(uuid);
        long player1Id = matchScoreDto.getIdPlayer1();
        long player2Id = matchScoreDto.getIdPlayer2();

        req.setAttribute("name1", playerDao.findById(player1Id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE))
                .getName());

        req.setAttribute("name2", playerDao.findById(player2Id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE))
                .getName());

        req.setAttribute("uuid", uuid);

    }

    private void setWinnerAttributes(HttpServletRequest req, UpdatedScoreDto updatedScoreDto) {

        long player1Id = updatedScoreDto.getUpdatedScore().getIdPlayer1();
        long player2Id = updatedScoreDto.getUpdatedScore().getIdPlayer2();
        long winnerId = updatedScoreDto.getWinner();

        req.setAttribute("name1", playerDao.findById(player1Id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE.formatted(player1Id)))
                .getName());

        req.setAttribute("name2", playerDao.findById(player2Id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE.formatted(player2Id)))
                .getName());

        req.setAttribute("winner", playerDao.findById(winnerId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NAME_ERROR_MESSAGE.formatted(winnerId)))
                .getName());

        req.setAttribute("match", updatedScoreDto.getUpdatedScore());

    }

}