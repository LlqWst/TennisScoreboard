package dev.lqwd.servlets;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.dto.ScoreForUpdatingDto;
import dev.lqwd.dto.UpdatedScoreDto;
import dev.lqwd.entity.Match;
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
public class MatchScoreServlet extends BasicServlet {

    private static final String MATCH_SCORE_URL = "match-score?uuid=%s";
    private static final MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
    private final PlayerDao playerDao = new PlayerDao();
    private static final FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

         UUID key = UUID.fromString(
                    req.getParameter("uuid")
            );

        log.info("uuid is redirected: {}}", key);

        req.setAttribute("name1", playerDao.findById(OngoingMatchesService.getInstance().getMatch(key).getIdPlayer1()).get().getName());
        req.setAttribute("name2", playerDao.findById(OngoingMatchesService.getInstance().getMatch(key).getIdPlayer2()).get().getName());
        req.setAttribute("games1", OngoingMatchesService.getInstance().getMatch(key).getGames1());
        req.setAttribute("games2", OngoingMatchesService.getInstance().getMatch(key).getGames2());
        req.setAttribute("sets1", OngoingMatchesService.getInstance().getMatch(key).getSets1());
        req.setAttribute("sets2", OngoingMatchesService.getInstance().getMatch(key).getSets2());
        req.setAttribute("points1", OngoingMatchesService.getInstance().getMatch(key).getPoints1());
        req.setAttribute("points2", OngoingMatchesService.getInstance().getMatch(key).getPoints2());
        req.setAttribute("uuid", key);
        req.getRequestDispatcher("match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UUID key = UUID.fromString(
                req.getParameter("uuid")
        );

        int position = Integer.parseInt(
                req.getParameter("player")
        );

        log.info("point for player number: {}}", position);

        ScoreForUpdatingDto updatingScoreDto = ScoreForUpdatingDto.builder()
                .matchScoreDto(OngoingMatchesService.getInstance().getMatch(key).toBuilder().build())
                .pointWinnerPosition(position)
                .build();

        UpdatedScoreDto updatedScoreDto = matchScoreCalculationService.updateScore(updatingScoreDto);

        if (updatedScoreDto.getIsWinner()){
           OngoingMatchesService.getInstance().removeMatch(key);
           Match match = finishedMatchesPersistenceService.saveMatch(updatedScoreDto);
            req.setAttribute("name1", playerDao.findById(match.getPlayer1().getId()).get().getName());
            req.setAttribute("name2", playerDao.findById(match.getPlayer2().getId()).get().getName());
            req.setAttribute("games1", updatedScoreDto.getUpdatedScore().getGames1());
            req.setAttribute("games2", updatedScoreDto.getUpdatedScore().getGames2());
            req.setAttribute("sets1", updatedScoreDto.getUpdatedScore().getSets1());
            req.setAttribute("sets2", updatedScoreDto.getUpdatedScore().getSets2());
            req.setAttribute("points1", updatedScoreDto.getUpdatedScore().getPoints1());
            req.setAttribute("points2", updatedScoreDto.getUpdatedScore().getPoints2());
            req.setAttribute("winner", playerDao.findById(match.getWinner().getId()).get().getName());
            req.getRequestDispatcher("match-score_winner.jsp").forward(req, resp);
        } else {
            OngoingMatchesService.getInstance().
                    updateScore(key, updatedScoreDto.getUpdatedScore());
            resp.sendRedirect(MATCH_SCORE_URL.formatted(key));

        }
    }

}