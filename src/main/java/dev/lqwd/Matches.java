package dev.lqwd;

import dev.lqwd.dto.MatchRequestDto;
import dev.lqwd.dto.PlayerServiceDto;
import dev.lqwd.entity.Player;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Getter
public class Matches {

    private static Map<UUID, Score> matches = new HashMap<>();

    private UUID key;
    private MatchRequestDto currentMatch;
    private Score score;

    public void add(PlayerServiceDto p1, PlayerServiceDto p2){

        key = UUID.randomUUID();

        log.info("key is generated: {}}", key);

        currentMatch = MatchRequestDto.builder()
                .player1(p1.getId())
                .player2(p2.getId())
                .build();

        score = Score.builder()
                .player1(0)
                .player2(0)
                .build();
    }

    public void remove(){
        matches.remove(key);

        if(!matches.containsKey(key)) {
            log.info("match is deleted: {}}", key);
        } else {
            log.warn("match is NOT deleted: {}}", key);
        }
    }

    public void appointWinner (Player winner){
        currentMatch.
                setWinner(winner.getId()
                );

    }

}
