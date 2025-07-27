package dev.lqwd.servlets;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.dto.ScoreForUpdatingDto;
import dev.lqwd.dto.ScoreUpdatedDto;
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
    private final MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
    private final PlayerDao playerDao = new PlayerDao();
    private UUID key;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        key = UUID.fromString(
                req.getParameter("uuid")
        );

        log.info("uuid is redirected: {}}", key);


        req.setAttribute("name1", playerDao.findById(OngoingMatchesService.getInstance().get(key).getIdPlayer1()).get().getName());
        req.setAttribute("name2", playerDao.findById(OngoingMatchesService.getInstance().get(key).getIdPlayer2()).get().getName());
        req.setAttribute("games1", OngoingMatchesService.getInstance().get(key).getGames1());
        req.setAttribute("games2", OngoingMatchesService.getInstance().get(key).getGames2());
        req.setAttribute("sets1", OngoingMatchesService.getInstance().get(key).getSets1());
        req.setAttribute("sets2", OngoingMatchesService.getInstance().get(key).getSets2());
        req.setAttribute("points1", OngoingMatchesService.getInstance().get(key).getPoints1());
        req.setAttribute("points2", OngoingMatchesService.getInstance().get(key).getPoints2());
        req.getRequestDispatcher("match-score.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int playerNumber = Integer.parseInt(
                req.getParameter("player")
        );

        log.info("point for player number: {}}", playerNumber);

        ScoreForUpdatingDto updatingScoreDto = ScoreForUpdatingDto.builder()
                .currentMatch(OngoingMatchesService.getInstance().get(key))
                .pointWinnerNumber(playerNumber)
                .build();

        ScoreUpdatedDto scoreUpdatedDto = matchScoreCalculationService.updateScore(updatingScoreDto);

        OngoingMatchesService.getInstance().get(key).setPoints1(scoreUpdatedDto.getCurrentMatch().getPoints1());
        OngoingMatchesService.getInstance().get(key).setPoints2(scoreUpdatedDto.getCurrentMatch().getPoints2());
        resp.sendRedirect(MATCH_SCORE_URL.formatted(key));
    }

}