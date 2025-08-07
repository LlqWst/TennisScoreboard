package dev.lqwd.service;

import dev.lqwd.dao.MatchesDao;
import dev.lqwd.dto.PaginationResponseDto;
import dev.lqwd.dto.finished_match.FinishedMatchRequestDto;
import dev.lqwd.entity.Match;
import dev.lqwd.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class FinishedMatchesService {

    private static final MatchesDao matchesDao = new MatchesDao();
    private final static int MAX_ELEMENTS_SIZE = 5;
    private final static int MAX_PAGES = 5;
    private final static String MAX_PAGES_MESSAGE = "For name %s, page %s is not available";


    public PaginationResponseDto getPaginationPages(String name, int page) {

        int maxPagesForName = (int) Math.ceil((double) matchesDao.countPlayedMatches(name) / MAX_ELEMENTS_SIZE);

        if (isNoMatchesForName(page, maxPagesForName) || isMoreThanPlayedMatches(page, maxPagesForName)) {

            log.warn("Max pages from BD less than requested. Page num is requested {}, max pages: {}", page, maxPagesForName);
            throw new BadRequestException(MAX_PAGES_MESSAGE.formatted(name, page));
        }

        int maxPage = Math.max(1, maxPagesForName);

        int firstPage = getFirstPage(page);
        int lastPage = getLastPage(page, maxPage);

        int prevList = Math.max(firstPage - 1, 1);
        int nextList = Math.min(lastPage + 1, maxPage);

        return PaginationResponseDto.builder()
                .firstPage(firstPage)
                .lastPage(lastPage)
                .prevList(prevList)
                .nextList(nextList)
                .build();
    }


    public List<Match> getFinishedMatches(FinishedMatchRequestDto finishedMatchRequestDto) {

        return matchesDao.findAllByFilters(finishedMatchRequestDto, MAX_ELEMENTS_SIZE)
                .orElseGet(Collections::emptyList);
    }

    private static int getFirstPage(int page) {

        return (int) Math.ceil((double) page / MAX_PAGES) * MAX_PAGES - MAX_PAGES + 1;
    }

    private static int getLastPage(int page, int maxPage) {

        int lastPage = (int) Math.ceil((double) page / MAX_PAGES) * MAX_PAGES;
        return Math.min(maxPage, lastPage);
    }

    private static boolean isMoreThanPlayedMatches(int page, int maxPage) {

        return maxPage != 0 && maxPage < page;

    }

    private static boolean isNoMatchesForName(int page, int maxPage) {

        return maxPage == 0 && page != 1;

    }

}
