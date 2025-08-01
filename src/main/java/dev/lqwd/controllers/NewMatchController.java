package dev.lqwd.controllers;

import dev.lqwd.service.OngoingMatchesService;
import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.NewMatchRequestDto;
import dev.lqwd.service.PlayerService;
import dev.lqwd.utils.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebServlet("/new-match")
public class NewMatchController extends BasicServlet {

    private static final PlayerService playerService = new PlayerService();
    private static final String MATCH_SCORE_URL = "match-score?uuid=%s";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("new-match.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String player1 = req.getParameter("player1Name");
        String player2 = req.getParameter("player2Name");

        Validator.validate(player1, player2);

        NewMatchRequestDto newMatchRequestDto = NewMatchRequestDto.builder()
                .player1(player1)
                .player2(player2)
                .build();

        MatchScoreDto matchScoreDto = playerService.getPLayersId(newMatchRequestDto);
        UUID key = OngoingMatchesService.getInstance().addMatch(matchScoreDto);

        log.info("matchScoreDto created uuid: {}}", key);

        resp.sendRedirect(MATCH_SCORE_URL.formatted(key));

    }

}