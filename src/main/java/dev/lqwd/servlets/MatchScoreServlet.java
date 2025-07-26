package dev.lqwd.servlets;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.dto.ScoreForUpdatingDto;
import dev.lqwd.dto.ScoreUpdatedDto;
import dev.lqwd.service.PlayerService;
import dev.lqwd.service.MatchScoreCalculationService;
import dev.lqwd.service.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@WebServlet("/match-score")
public class MatchScoreServlet extends BasicServlet {

    private static final String MATCH_SCORE_URL = "match-score?uuid=%s";
    private final MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
    private final PlayerDao playerDao = new PlayerDao();
    private UUID key;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        key = UUID.fromString(
                req.getParameter("uuid")
        );

        log.info("uuid is redirected: {}}", key);

        req.setAttribute("player1", OngoingMatchesService.getInstance().get(key).getIdPlayer1());
        req.setAttribute("player2", OngoingMatchesService.getInstance().get(key).getIdPlayer2());
        req.setAttribute("name1", playerDao.findById(OngoingMatchesService.getInstance().get(key).getIdPlayer1()).get().getName());
        req.setAttribute("name2", playerDao.findById(OngoingMatchesService.getInstance().get(key).getIdPlayer2()).get().getName());
        req.setAttribute("score1", OngoingMatchesService.getInstance().get(key).getScore1());
        req.setAttribute("score2", OngoingMatchesService.getInstance().get(key).getScore2());
        req.getRequestDispatcher("match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long pointForId = Long.parseLong(
                req.getParameter("id")
        );

        log.info("point for id: {}}", pointForId);

        ScoreForUpdatingDto updatingScoreDto = ScoreForUpdatingDto.builder()
                .currentMatch(OngoingMatchesService.getInstance().get(key))
                .pointForId(pointForId)
                .build();

        ScoreUpdatedDto scoreUpdatedDto = matchScoreCalculationService.updateScore(updatingScoreDto);
        OngoingMatchesService.getInstance().get(key).setScore1(scoreUpdatedDto.getCurrentMatch().getScore1());
        OngoingMatchesService.getInstance().get(key).setScore2(scoreUpdatedDto.getCurrentMatch().getScore2());
        resp.sendRedirect(MATCH_SCORE_URL.formatted(key));
    }

}