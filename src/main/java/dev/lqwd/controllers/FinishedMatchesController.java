package dev.lqwd.controllers;

import dev.lqwd.FinishedMatchMapper;
import dev.lqwd.dto.FinishedMatchResponseDto;
import dev.lqwd.dto.FinishedMatchRequestDto;
import dev.lqwd.service.FinishedMatchesService;
import dev.lqwd.utils.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/matches")
public class FinishedMatchesController extends BasicServlet {

    private static final int DEFAULT_PAGE = 1;
    private static final String EMPTY_NAME = "";
    private static final FinishedMatchesService FINISHED_MATCHES_SERVICE = new FinishedMatchesService();
    private static final FinishedMatchMapper mapper = FinishedMatchMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int page = Validator.parseParameter(
                req.getParameter("page"), DEFAULT_PAGE);

        String name = Validator.parseParameter(
                req.getParameter("filter_by_player_name"), EMPTY_NAME);

        FinishedMatchRequestDto finishedMatchRequestDto = FinishedMatchRequestDto.builder()
                .page(page)
                .name(name)
                .build();

        int pages = FINISHED_MATCHES_SERVICE.getMaxPages(name);

        List<FinishedMatchResponseDto> finishedMatchesResponseDto = FINISHED_MATCHES_SERVICE
                .getFinishedMatches(finishedMatchRequestDto)
                .stream()
                .map(mapper::toMatchResponseDto)
                .toList();

        req.setAttribute("allFinishedMatches", finishedMatchesResponseDto);
        req.setAttribute("pages", pages);


        req.getRequestDispatcher("finishedMatches.jsp").forward(req, resp);

    }

}