package dev.lqwd.service;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.dto.MatchScoreDto;
import dev.lqwd.dto.NewMatchRequestDto;
import dev.lqwd.entity.Player;

import java.util.Optional;

public class PlayerService {

    private final PlayerDao playerDao = new PlayerDao();

    public MatchScoreDto findPLayersId(NewMatchRequestDto newMatchRequestDto){

        String player1 = newMatchRequestDto.getPlayer1();
        String player2 = newMatchRequestDto.getPlayer2();

        Long idPlayer1 = playerDao.findByName(player1)
                .orElseGet(() -> playerDao.save(player1))
                .getId();

        Long idPlayer2 = playerDao.findByName(player2)
                .orElseGet(() -> playerDao.save(player2))
                .getId();

        return MatchScoreDto.builder()
                .idPlayer1(idPlayer1)
                .idPlayer2(idPlayer2)
                .build();

    }

}
