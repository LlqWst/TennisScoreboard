package dev.lqwd.service;

import dev.lqwd.dao.MatchesDao;
import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.entity.Match;
import dev.lqwd.entity.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinishedMatchesPersistenceService {

    private static final MatchesDao matchesDao = new MatchesDao();

    public Match saveMatch(MatchScoreDto MatchScoreUpdatedDto){

        return matchesDao.save(Match.builder()
                .player1(Player.builder()
                        .id(MatchScoreUpdatedDto.getIdPlayer1())
                        .build())
                .player2(Player.builder()
                        .id(MatchScoreUpdatedDto.getIdPlayer2())
                        .build())
                .winner(Player.builder()
                        .id(MatchScoreUpdatedDto.getIdWinner())
                        .build())
                .build()
        );

    }

}
