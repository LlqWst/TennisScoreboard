package dev.lqwd.service;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.NewMatchRequestDto;
import dev.lqwd.entity.Player;
import dev.lqwd.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class NewMatchPlayersService {

    private static final String ERROR_EQUALS_MESSAGE = "Names are equal, Validator couldn't do the job for names: %s, %s";
    private final PlayerDao playerDao = new PlayerDao();

    public MatchScoreDto getPLayers(NewMatchRequestDto newMatchRequestDto) {

        String player1 = newMatchRequestDto.getPlayer1Name();
        String player2 = newMatchRequestDto.getPlayer2Name();

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

        if(Objects.equals(Player1.getId(), Player2.getId())){

            log.error("Names are equals: {}, {}", Player1.getName(), Player2.getName());
            throw new BadRequestException(ERROR_EQUALS_MESSAGE.formatted(Player1.getName(), Player2.getName()));

        }


        return MatchScoreDto.builder()
                .player1(Player1)
                .player2(Player2)
                .build();

    }

}
