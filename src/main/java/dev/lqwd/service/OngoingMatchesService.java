package dev.lqwd.service;

import dev.lqwd.dto.MatchScoreDto;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class OngoingMatchesService {

    private static OngoingMatchesService instance;
    private final Map<UUID, MatchScoreDto> matches;

    private OngoingMatchesService() {
        matches = new HashMap<>();
    }

    public static synchronized  OngoingMatchesService getInstance(){
        if(instance == null){
            instance = new OngoingMatchesService();
        }
        return instance;
    }

    public UUID addMatch(MatchScoreDto currentMatch) {

        UUID key = UUID.randomUUID();

        matches.put(key, currentMatch);

        log.info("match is created: {}}", currentMatch);

        return key;

    }

    public void removeMatch(UUID key) {
        matches.remove(key);

        if (!matches.containsKey(key)) {
            log.info("match is deleted: {}}", key);
        } else {
            log.error("match is NOT deleted: {}}", key);
        }

    }

    public void updateScore(UUID key, MatchScoreDto matchScoreDto) {
        matches.put(key, matchScoreDto);
    }

    public MatchScoreDto getMatch(UUID key) {

        return matches.get(key);

    }

}
