package dev.lqwd.service;

import dev.lqwd.dao.MatchesDao;
import dev.lqwd.dto.MatchFilterRequestDto;
import dev.lqwd.entity.Match;

import java.util.List;

public class FilterMatchesService {

    private static final MatchesDao matchesDao = new MatchesDao();
    private final static int MAX_SIZE = 4;


    public int getMaxPages(String name){

        return (int) Math.ceil( (double) matchesDao.countPlayedMatches(name) / MAX_SIZE);

    }

    public List<Match> getMatchesByFilters(MatchFilterRequestDto matchFilterRequestDto){

        matchFilterRequestDto.setMaxSize(MAX_SIZE);
        return matchesDao.findAllByFilters(matchFilterRequestDto);

    }

}
