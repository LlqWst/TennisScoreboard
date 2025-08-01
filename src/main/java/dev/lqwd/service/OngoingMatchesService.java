package dev.lqwd.service;

import dev.lqwd.dto.match_score.MatchScoreDto;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class OngoingMatchesService {

    private static OngoingMatchesService instance;
    private final Map<UUID, MatchScoreDto> matches;

    private OngoingMatchesService() {
        matches = new HashMap<>();
    }

    public static OngoingMatchesService getInstance() {

        if (instance == null) {
            instance = new OngoingMatchesService();
        }

        return instance;
    }

    public UUID addMatch(MatchScoreDto currentMatch) {

        UUID key = UUID.randomUUID();

        matches.put(key, currentMatch);

        log.info("matchScoreDto is created: {}}", currentMatch);

        return key;

    }

    public void removeMatch(UUID key) {
        matches.remove(key);

        if (!isContainsKey(key)) {
            log.info("matchScoreDto is deleted: {}}", key);
        } else {
            log.error("matchScoreDto is NOT deleted: {}}", key);
            throw new RuntimeException("matchScoreDto is NOT deleted: %s".formatted(key));
        }

    }

    public boolean isContainsKey(UUID key){
        return matches.containsKey(key);
    }

    public void updateScore(UUID key, MatchScoreDto matchScoreDto) {

        matches.put(key, matchScoreDto);

    }

    public MatchScoreDto getMatchScoreDto(UUID key) {

        if (isContainsKey(key)){
            return matches.get(key);
        }
        log.error("Match with uuid: {} don't exist", key);
        throw new RuntimeException("There is no match with uuid: %s".formatted(key));


    }

}
