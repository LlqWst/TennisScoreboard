package dev.lqwd.controllers;

import dev.lqwd.dto.MatchFilterRequestDto;
import dev.lqwd.entity.Match;
import dev.lqwd.service.FilterMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/matches")
public class PlayedMatchesController extends BasicServlet {

    private static final FilterMatchesService filterMatchesService = new FilterMatchesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pageParameter = req.getParameter("page");
        String name = req.getParameter("filter_by_player_name");

        int page = 1;

        if (pageParameter != null) {
            page = Integer.parseInt(pageParameter);
        }


        MatchFilterRequestDto matchFilterRequestDto = MatchFilterRequestDto.builder()
                .page(page)
                .name(name)
                .build();

        int maxPages = filterMatchesService.getMaxPages(name);
        List<Match> matches = filterMatchesService.getMatchesByFilters(matchFilterRequestDto);

        log.info("page is caught: {}}", name);

        req.getRequestDispatcher("match-score.jsp").forward(req, resp);

    }

}