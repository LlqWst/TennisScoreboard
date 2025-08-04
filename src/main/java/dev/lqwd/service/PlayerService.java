package dev.lqwd.service;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.NewMatchRequestDto;
import dev.lqwd.dto.match_score.Score;
import dev.lqwd.entity.Player;


public class PlayerService {

    private final PlayerDao playerDao = new PlayerDao();

    public MatchScoreDto getPLayers(NewMatchRequestDto newMatchRequestDto) {

        String player1 = newMatchRequestDto.getPlayer1();
        String player2 = newMatchRequestDto.getPlayer2();

        Player Player1 = playerDao.findByName(player1)
                .orElseGet(() -> playerDao.save(
                        Player.builder()
                                .name(player1)
                                .build())
                );

        Player Player2 = playerDao.findByName(player2)
                .orElseGet(() -> playerDao.save(
                        Player.builder()
                                .name(player2)
                                .build())
                );

        return MatchScoreDto.builder()
                .player1(Player1)
                .player2(Player2)
                .build();

    }

}
