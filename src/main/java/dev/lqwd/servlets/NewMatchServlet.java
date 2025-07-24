package dev.lqwd.servlets;

import dev.lqwd.utils.Matches;
import dev.lqwd.dto.MatchScoreDto;
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
public class NewMatchServlet extends BasicServlet {

    private static final PlayerService playerService = new PlayerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String player1 = request.getParameter("player1");
        String player2 = request.getParameter("player2");

        Validator.validate(player1, player2);

        NewMatchRequestDto newMatchRequestDto = NewMatchRequestDto.builder()
                .player1(player1)
                .player2(player2)
                .build();

        MatchScoreDto matchScoreDto = playerService.findPLayersId(newMatchRequestDto);
        UUID key = Matches.add(matchScoreDto);

        doResponse(response, 200, Matches.getAll());

    }

}