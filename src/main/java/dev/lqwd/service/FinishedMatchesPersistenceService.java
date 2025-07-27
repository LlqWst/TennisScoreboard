package dev.lqwd.service;

import dev.lqwd.dao.MatchesDao;
import dev.lqwd.dto.ScoreUpdatedDto;
import dev.lqwd.entity.Match;
import dev.lqwd.entity.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinishedMatchesPersistenceService {

    private static final MatchesDao matchesDao = new MatchesDao();

    public Match saveMatch(ScoreUpdatedDto scoreUpdatedDto){

        return matchesDao.save(Match.builder()
                .player1(Player.builder()
                        .id(scoreUpdatedDto.getUpdatedScore().getIdPlayer1())
                        .build())
                .player2(Player.builder()
                        .id(scoreUpdatedDto.getUpdatedScore().getIdPlayer2())
                        .build())
                .winner(Player.builder()
                        .id(scoreUpdatedDto.getWinner())
                        .build())
                .build()
        );

    }

}
