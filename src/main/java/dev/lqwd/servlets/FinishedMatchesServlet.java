package dev.lqwd.servlets;

import dev.lqwd.FinishedMatchMapper;
import dev.lqwd.dto.PaginationResponseDto;
import dev.lqwd.dto.finished_match.FinishedMatchResponseDto;
import dev.lqwd.dto.finished_match.FinishedMatchRequestDto;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.service.FinishedMatchesService;
import dev.lqwd.utils.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@WebServlet("/matches")
public class FinishedMatchesServlet extends HttpServlet {

    private static final int DEFAULT_PAGE = 1;
    private static final String EMPTY_NAME = "";
    private static final FinishedMatchesService FINISHED_MATCHES_SERVICE = new FinishedMatchesService();
    private static final FinishedMatchMapper mapper = FinishedMatchMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int page;
        String name;

        try {

            page = Validator.parseParameter(
                    req.getParameter("page"), DEFAULT_PAGE);

            name = Validator.parseParameter(
                    req.getParameter("filter_by_player_name"), EMPTY_NAME);

        } catch (BadRequestException e){
            redirectToMatchesUrl(req, resp);
            return;
        }

        FinishedMatchRequestDto finishedMatchRequestDto = FinishedMatchRequestDto.builder()
                .page(page)
                .name(name)
                .build();

        List<FinishedMatchResponseDto> finishedMatchesResponseDto = FINISHED_MATCHES_SERVICE
                .getFinishedMatches(finishedMatchRequestDto)
                .stream()
                .map(mapper::toMatchResponseDto)
                .toList();

        PaginationResponseDto paginationResponseDto;

        try {

            paginationResponseDto = FINISHED_MATCHES_SERVICE.getPaginationPages(name, page);

        } catch (BadRequestException e){
            redirectToMatchesUrl(req, resp);
            return;
        }

        req.setAttribute("finishedMatches", finishedMatchesResponseDto);
        req.setAttribute("pages", paginationResponseDto);

        req.getRequestDispatcher("finishedMatches.jsp").forward(req, resp);

    }

    private static void redirectToMatchesUrl(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/matches");
    }

}