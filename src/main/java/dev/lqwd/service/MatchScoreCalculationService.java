package dev.lqwd.service;

import dev.lqwd.dto.MatchScoreDto;
import dev.lqwd.dto.ScoreForUpdatingDto;
import dev.lqwd.dto.ScoreUpdatedDto;

public class MatchScoreCalculationService {

    private static final String ZERO_STRING = "0";
    private static final String ADVANTAGE = "Adv";
    private static final String SCORE_FORTY = "40";
    private static final String THREE_POINTS = "45";
    private static final String FOUR_POINTS = "55";
    private static final int ZERO_INT = 0;
    private static final int POINT = 1;
    private static final int SCORE_POINTS = 15;
    private static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;
    private static final int MIN_GAMES_POINTS = 6;
    private static final int MIN_TIE_BREAK_POINTS = 7;
    private static final int MIN_TIE_BREAK_POINTS_DIFF = 2;
    private static final int SETS_TO_WIN = 2;

    public ScoreUpdatedDto updateScore(ScoreForUpdatingDto scoreForUpdatingDto) {

        MatchScoreDto matchScoreDto = scoreForUpdatingDto.getMatchScoreDto();
        int pointWinner = scoreForUpdatingDto.getPointWinnerPosition();
        int nextPlayer = findSecondPlayerPosition(pointWinner);

        ScoreUpdatedDto scoreUpdatedDto;

        if (matchScoreDto.isTieBreak()) {
            scoreUpdatedDto = updateScoreBasedOnTieBreak(matchScoreDto, pointWinner, nextPlayer);
        } else {
            scoreUpdatedDto = updateScorePlayers(matchScoreDto, pointWinner, nextPlayer);
        }

        if(isWinner(matchScoreDto, pointWinner)){
            scoreUpdatedDto.setIsWinner(true);
            scoreUpdatedDto.setWinner(matchScoreDto.getIdByPosition(pointWinner));
        }

        return scoreUpdatedDto;

    }

    private int findSecondPlayerPosition(int pointWinner){
        return pointWinner == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }

    private ScoreUpdatedDto updateScoreBasedOnTieBreak(MatchScoreDto matchScoreDto, int pointWinner, int nextPlayer) {

        int gamePointsWinner = matchScoreDto.getGamesByPosition(pointWinner) + POINT;
        int gamePointsNextPlayer = matchScoreDto.getGamesByPosition(nextPlayer);
        int pointsDiff = Math.abs(gamePointsWinner - gamePointsNextPlayer);

        if (gamePointsWinner >= MIN_TIE_BREAK_POINTS && pointsDiff >= MIN_TIE_BREAK_POINTS_DIFF) {

            int SetsPointsWinner = matchScoreDto.getSetsByPosition(pointWinner) + POINT;

            matchScoreDto.setSetsByPosition(pointWinner, SetsPointsWinner);
            matchScoreDto.setGamesByPosition(pointWinner, ZERO_INT);
            matchScoreDto.setGamesByPosition(nextPlayer, ZERO_INT);
            matchScoreDto.setTieBreak(false);

        } else {

            matchScoreDto.setGamesByPosition(pointWinner, gamePointsWinner);
            matchScoreDto.setTieBreak(true);

        }
        return ScoreUpdatedDto.builder()
                .updatedScore(matchScoreDto)
                .build();

    }

    private ScoreUpdatedDto updateScorePlayers(MatchScoreDto matchScoreDto, int pointWinner, int nextPlayer) {

        if (matchScoreDto.getPointsByPosition(pointWinner).equals(ADVANTAGE)) {

            matchScoreDto.setGamesByPosition(pointWinner, matchScoreDto.getGamesByPosition(pointWinner) + POINT);
            matchScoreDto.setPointsByPosition(pointWinner, ZERO_STRING);
            matchScoreDto.setPointsByPosition(nextPlayer, ZERO_STRING);

        } else if (matchScoreDto.getPointsByPosition(nextPlayer).equals(ADVANTAGE)) {

            matchScoreDto.setPointsByPosition(nextPlayer, SCORE_FORTY);

        } else {

            String winnerPoints = String.valueOf(
                    Integer.parseInt(matchScoreDto.getPointsByPosition(pointWinner)) + SCORE_POINTS);
            String nextPoints = matchScoreDto.getPointsByPosition(nextPlayer);

            if (winnerPoints.equals(FOUR_POINTS) && nextPoints.equals(SCORE_FORTY)) {

                matchScoreDto.setPointsByPosition(pointWinner, ADVANTAGE);

            } else if (winnerPoints.equals(THREE_POINTS)) {

                matchScoreDto.setPointsByPosition(pointWinner, SCORE_FORTY);

            } else if (winnerPoints.equals(FOUR_POINTS)) {

                matchScoreDto.setGamesByPosition(pointWinner, matchScoreDto.getGamesByPosition(pointWinner) + POINT);
                matchScoreDto.setPointsByPosition(pointWinner, ZERO_STRING);
                matchScoreDto.setPointsByPosition(nextPlayer, ZERO_STRING);

            } else {

                matchScoreDto.setPointsByPosition(pointWinner, winnerPoints);

            }
        }

        if (matchScoreDto.getGamesByPosition(pointWinner) == MIN_GAMES_POINTS && matchScoreDto.getGamesByPosition(nextPlayer) == MIN_GAMES_POINTS) {

            matchScoreDto.setGamesByPosition(pointWinner, ZERO_INT);
            matchScoreDto.setGamesByPosition(nextPlayer, ZERO_INT);
            matchScoreDto.setPointsByPosition(pointWinner, ZERO_STRING);
            matchScoreDto.setPointsByPosition(nextPlayer, ZERO_STRING);
            matchScoreDto.setTieBreak(true);

        } else if (matchScoreDto.getGamesByPosition(pointWinner) == MIN_GAMES_POINTS && matchScoreDto.getGamesByPosition(nextPlayer) < 5
                   || matchScoreDto.getGamesByPosition(pointWinner) > MIN_GAMES_POINTS /*&& matchScoreDto.getGamesByPosition(nextPlayer) == 5*/) {

            matchScoreDto.setSetsByPosition(pointWinner, matchScoreDto.getSetsByPosition(pointWinner) + POINT);
            matchScoreDto.setGamesByPosition(pointWinner, ZERO_INT);
            matchScoreDto.setGamesByPosition(nextPlayer, ZERO_INT);
            matchScoreDto.setPointsByPosition(pointWinner, ZERO_STRING);
            matchScoreDto.setPointsByPosition(nextPlayer, ZERO_STRING);

        }

        return ScoreUpdatedDto.builder()
                .updatedScore(matchScoreDto)
                .build();

    }

    private Boolean isWinner (MatchScoreDto matchScoreDto, int pointWinner) {
        return matchScoreDto.getSetsByPosition(pointWinner) == SETS_TO_WIN;
    }

}
