package dev.lqwd.service;

import dev.lqwd.dto.MatchScoreDto;
import dev.lqwd.dto.ScoreForUpdatingDto;
import dev.lqwd.dto.ScoreUpdatedDto;

public class MatchScoreCalculationService {

    public ScoreUpdatedDto updateScore(ScoreForUpdatingDto scoreForUpdatingDto){

        MatchScoreDto matchScoreDto = scoreForUpdatingDto.getCurrentMatch();
        long id = scoreForUpdatingDto.getPointForId();

        if (id == matchScoreDto.getIdPlayer1()){
            matchScoreDto.setScore1(matchScoreDto.getScore1() + 15);
        } else {
            matchScoreDto.setScore2(matchScoreDto.getScore2() + 15);
        }

        return ScoreUpdatedDto.builder()
                .currentMatch(matchScoreDto)
                .build();
    }

}
