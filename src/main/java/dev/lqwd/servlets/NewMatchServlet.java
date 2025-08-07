package dev.lqwd.servlets;

import dev.lqwd.exception.BadRequestException;
import dev.lqwd.service.OngoingMatchesService;
import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.NewMatchRequestDto;
import dev.lqwd.service.NewMatchPlayersService;
import dev.lqwd.utils.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Slf4j
@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {

    private static final NewMatchPlayersService NEW_MATCH_PLAYERS_SERVICE = new NewMatchPlayersService();
    private static final String MATCH_SCORE_URL = "match-score?uuid=%s";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("new-match.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String player1Name = req.getParameter("player1Name");
        String player2Name = req.getParameter("player2Name");

        try {

            Validator.validate(player1Name, player2Name);

        } catch (BadRequestException e) {

            req.setAttribute("error", e.getMessage());
            resp.setStatus(SC_BAD_REQUEST);
            req.getRequestDispatcher("new-match.jsp").forward(req, resp);
            return;

        }

        NewMatchRequestDto newMatchRequestDto = NewMatchRequestDto.builder()
                .player1Name(player1Name.trim())
                .player2Name(player2Name.trim())
                .build();

        MatchScoreDto matchScoreDto = NEW_MATCH_PLAYERS_SERVICE.getPLayers(newMatchRequestDto);
        UUID key = OngoingMatchesService.getInstance().addMatch(matchScoreDto);

        log.info("matchScoreDto created uuid: {}}", key);

        resp.sendRedirect(MATCH_SCORE_URL.formatted(key));

    }

}