package dev.lqwd.controllers;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.dto.ScoreForUpdatingDto;
import dev.lqwd.dto.UpdatedScoreDto;
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
    private static final MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
    private static final PlayerDao playerDao = new PlayerDao();
    private static final FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UUID uuid = UUID.fromString(
                req.getParameter("uuid"));

        log.info("uuid is redirected: {}}", uuid);

        setGetParameters(req, uuid);

        req.getRequestDispatcher("match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UUID uuid = UUID.fromString(
                req.getParameter("uuid"));

        int pointWinnerNumber = Integer.parseInt(
                req.getParameter("playerNumber"));

        log.info("point for player number: {}}", pointWinnerNumber);

        ScoreForUpdatingDto updatingScoreDto = ScoreForUpdatingDto.builder()
                .matchScoreDto(OngoingMatchesService.getInstance().getMatch(uuid).toBuilder().build())
                .pointWinnerNumber(pointWinnerNumber)
                .build();

        UpdatedScoreDto updatedScoreDto = matchScoreCalculationService.updateScore(updatingScoreDto);

        if (updatedScoreDto.getIsWinner()){

            OngoingMatchesService.getInstance().removeMatch(uuid);
            finishedMatchesPersistenceService.saveMatch(updatedScoreDto);

            setPostParameters(req, updatedScoreDto);
            req.getRequestDispatcher("match-score_winner.jsp").forward(req, resp);

        } else {

            OngoingMatchesService.getInstance().updateScore(uuid, updatedScoreDto.getUpdatedScore());

            log.info("score updated for uuid: {}, score: {} }", uuid, updatedScoreDto);

            resp.sendRedirect(MATCH_SCORE_URL.formatted(uuid));
        }

    }

    private void setGetParameters(HttpServletRequest req, UUID uuid){
        req.setAttribute("match", OngoingMatchesService.getInstance().getMatch(uuid));
        req.setAttribute("numberPlayer1", 1);
        req.setAttribute("numberPlayer2", 2);
        req.setAttribute("name1", playerDao.findById(OngoingMatchesService.getInstance().getMatch(uuid).getIdPlayer1()).get().getName());
        req.setAttribute("name2", playerDao.findById(OngoingMatchesService.getInstance().getMatch(uuid).getIdPlayer2()).get().getName());
        req.setAttribute("uuid", uuid);
    }

    private void setPostParameters(HttpServletRequest req, UpdatedScoreDto updatedScoreDto){
        req.setAttribute("name1", playerDao.findById(updatedScoreDto.getUpdatedScore().getIdPlayer1()).get().getName());
        req.setAttribute("name2", playerDao.findById(updatedScoreDto.getUpdatedScore().getIdPlayer2()).get().getName());
        req.setAttribute("winner", playerDao.findById(updatedScoreDto.getWinner()).get().getName());
        req.setAttribute("match", updatedScoreDto.getUpdatedScore());
    }

}