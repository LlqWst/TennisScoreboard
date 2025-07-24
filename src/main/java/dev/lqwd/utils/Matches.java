package dev.lqwd.utils;

import dev.lqwd.dto.MatchScoreDto;
import dev.lqwd.dto.MatchesCurrentDto;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public final class Matches {

    private Matches() {
    }

    private static Map<UUID, MatchScoreDto> matches = new HashMap<>();

    public static UUID add(MatchScoreDto currentMatch) {

        UUID key = UUID.randomUUID();

        matches.put(key, currentMatch);

        log.info("match is created: {}}", currentMatch);

        return key;

    }

    public static void remove(UUID key) {
        matches.remove(key);

        if (!matches.containsKey(key)) {
            log.info("match is deleted: {}}", key);
        } else {
            log.error("match is NOT deleted: {}}", key);
        }

    }

    public static MatchScoreDto get(UUID key) {

        return matches.get(key);

    }

    public static List<MatchesCurrentDto> getAll(){

        List<MatchesCurrentDto> list = new ArrayList<>();

        for(Map.Entry<UUID, MatchScoreDto> entry : matches.entrySet()){
            MatchesCurrentDto dto = MatchesCurrentDto.builder()
                    .id(entry.getKey())
                    .id1(entry.getValue().getIdPlayer1())
                    .id2(entry.getValue().getIdPlayer2())
                    .build();
            list.add(dto);
        }
        return list;
//Map.Entry<Coordinates, Entity> entry : entities.entrySet()
    }

}
