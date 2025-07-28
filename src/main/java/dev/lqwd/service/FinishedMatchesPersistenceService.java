package dev.lqwd.service;

import dev.lqwd.dao.MatchesDao;
import dev.lqwd.dto.UpdatedScoreDto;
import dev.lqwd.entity.Match;
import dev.lqwd.entity.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinishedMatchesPersistenceService {

    private static final MatchesDao matchesDao = new MatchesDao();

    public Match saveMatch(UpdatedScoreDto updatedScoreDto){

        return matchesDao.save(Match.builder()
                .player1(Player.builder()
                        .id(updatedScoreDto.getUpdatedScore().getIdPlayer1())
                        .build())
                .player2(Player.builder()
                        .id(updatedScoreDto.getUpdatedScore().getIdPlayer2())
                        .build())
                .winner(Player.builder()
                        .id(updatedScoreDto.getWinner())
                        .build())
                .build()
        );

    }

}
