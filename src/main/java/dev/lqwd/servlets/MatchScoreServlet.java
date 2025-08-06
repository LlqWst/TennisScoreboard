package dev.lqwd.servlets;

import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.match_score.MatchScoreForUpdateRequestDto;
import dev.lqwd.dto.match_score.MatchScoreResponseDto;
import dev.lqwd.service.FinishedMatchesPersistenceService;
import dev.lqwd.service.MatchScoreCalculationService;
import dev.lqwd.service.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    private static final String MATCH_SCORE_URL = "match-score?uuid=%s";
    private static final MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
    private static final FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService();
    private static final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();
    private static final int PLAYER_1_NUMBER = 1;
    private static final int PLAYER_2_NUMBER = 2;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UUID uuid = UUID.fromString(
                req.getParameter("uuid"));

        log.info("uuid is redirected: {}}", uuid);

        MatchScoreDto matchScoreDto = ongoingMatchesService.getMatchScoreDto(uuid);

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
                .matchScoreDto(ongoingMatchesService.getMatchScoreDto(uuid).toBuilder().build())
                .pointWinnerNumber(pointWinnerNumber)
                .build();

        MatchScoreDto matchScoreDtoUpdated = matchScoreCalculationService.calculateScore(matchScoreForUpdateRequestDto);

        if (isWinner(matchScoreDtoUpdated)) {

            ongoingMatchesService.removeMatch(uuid);
            finishedMatchesPersistenceService.saveMatch(matchScoreDtoUpdated);

            log.trace("matched removed and saved: {}}", matchScoreDtoUpdated);

            req.setAttribute("matchScore", setUpdatedMatchScore(matchScoreDtoUpdated));
            req.getRequestDispatcher("match-winner.jsp").forward(req, resp);

        } else {

            OngoingMatchesService.getInstance().updateScore(uuid, matchScoreDtoUpdated);

            log.info("score updated for uuid: {}, score: {} }", uuid, matchScoreDtoUpdated);

            resp.sendRedirect(MATCH_SCORE_URL.formatted(uuid));
        }

    }

    private static boolean isWinner(MatchScoreDto matchScoreDtoUpdated) {

        return matchScoreDtoUpdated.getWinner() != null;

    }

    private static MatchScoreResponseDto setUpdatedMatchScore(MatchScoreDto matchScoreDto) {

        return MatchScoreResponseDto.builder()
                .matchScoreDto(matchScoreDto)
                .numberPlayer1(PLAYER_1_NUMBER)
                .numberPlayer2(PLAYER_2_NUMBER)
                .build();

    }

}