package dev.lqwd.servlets;

import dev.lqwd.mapper.MatchScoreMapper;
import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.match_score.MatchScoreForUpdateRequestDto;
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
    private static final MatchScoreMapper mapper = MatchScoreMapper.INSTANCE;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UUID uuid = UUID.fromString(
                req.getParameter("uuid"));

        MatchScoreDto matchScoreDto = ongoingMatchesService.getMatchScoreDto(uuid);

        req.setAttribute("matchScore", mapper.toMatchScoreResponseDto(matchScoreDto));
        req.getRequestDispatcher("match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = UUID.fromString(
                req.getParameter("uuid"));

        if (!ongoingMatchesService.isContainsKey(uuid)) {

            log.warn("Redirect from post method - servlet 'new-matches'");
            resp.sendRedirect(req.getContextPath() + "/");
            return;

        }

        long pointWinnerId = Long.parseLong(
                req.getParameter("winnerPointId"));

        MatchScoreForUpdateRequestDto matchScoreForUpdateRequestDto = MatchScoreForUpdateRequestDto.builder()
                .matchScoreDto(ongoingMatchesService.getMatchScoreDto(uuid).toBuilder().build())
                .pointWinnerId(pointWinnerId)
                .build();

        MatchScoreDto matchScoreDtoUpdated = matchScoreCalculationService.calculateScore(matchScoreForUpdateRequestDto);

        if (isWinner(matchScoreDtoUpdated)) {

            ongoingMatchesService.removeMatch(uuid);
            finishedMatchesPersistenceService.saveMatch(matchScoreDtoUpdated);

            log.trace("matched removed and saved: {}}", matchScoreDtoUpdated);

            req.setAttribute("matchScore", mapper.toMatchScoreResponseDto(matchScoreDtoUpdated));
            req.getRequestDispatcher("match-winner.jsp").forward(req, resp);

        } else {

            ongoingMatchesService.updateScore(uuid, matchScoreDtoUpdated);

            resp.sendRedirect(MATCH_SCORE_URL.formatted(uuid));
        }

    }

    private static boolean isWinner(MatchScoreDto matchScoreDtoUpdated) {

        return matchScoreDtoUpdated.getWinner() != null;

    }

}