package dev.lqwd.service;

import dev.lqwd.dao.MatchesDao;
import dev.lqwd.dto.finished_match.FinishedMatchRequestDto;
import dev.lqwd.entity.Match;

import java.util.Collections;
import java.util.List;

public class FinishedMatchesService {

    private static final MatchesDao matchesDao = new MatchesDao();
    private final static int MAX_SIZE = 4;


    public int getMaxPages(String name) {

        return (int) Math.ceil((double) matchesDao.countPlayedMatches(name) / MAX_SIZE);

    }

    public List<Match> getFinishedMatches(FinishedMatchRequestDto finishedMatchRequestDto) {

        finishedMatchRequestDto.setMaxSize(MAX_SIZE);

        return matchesDao.findAllByFilters(finishedMatchRequestDto)
                .orElseGet(Collections::emptyList);

    }

}
