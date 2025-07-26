package dev.lqwd.service;

import dev.lqwd.dto.MatchScoreDto;
import dev.lqwd.dto.ScoreForUpdatingDto;
import dev.lqwd.dto.ScoreUpdatedDto;

public class MatchScoreCalculationService {

    public ScoreUpdatedDto updateScore(ScoreForUpdatingDto scoreForUpdatingDto){

        MatchScoreDto matchScoreDto = scoreForUpdatingDto.getCurrentMatch();
        long id = scoreForUpdatingDto.getPointForId();

        return ScoreUpdatedDto.builder().build();
    }

}
