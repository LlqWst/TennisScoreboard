package dev.lqwd.service;

import dev.lqwd.dto.MatchScoreDto;
import dev.lqwd.dto.ScoreForUpdatingDto;
import dev.lqwd.dto.UpdatedScoreDto;

public class MatchScoreCalculationService {

    private static final String ZERO_STRING = "0";
    private static final String ADVANTAGE = "Ad";
    private static final String FORTY = "40";
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
    private static final int MIN_DIFF_FOR_SET_POINT = MIN_GAMES_POINTS - 2;

    private static int pointWinner;
    private static int nextPlayer;

    public UpdatedScoreDto updateScore(ScoreForUpdatingDto scoreForUpdatingDto) {

        pointWinner = scoreForUpdatingDto.getPointWinnerPosition();
        nextPlayer = findSecondPlayerPosition(pointWinner);
        MatchScoreDto matchScoreDto = scoreForUpdatingDto.getMatchScoreDto();

        UpdatedScoreDto updatedScoreDto;

        if (matchScoreDto.isTieBreak()) {
            updatedScoreDto = updateScoreBasedOnTieBreak(matchScoreDto);
        } else {
            updatedScoreDto = updateScorePlayers(matchScoreDto);
        }

        if (isWinner(matchScoreDto)) {
            updatedScoreDto.setIsWinner(true);
            updatedScoreDto.setWinner(matchScoreDto.getIdByPosition(pointWinner));
        }

        return updatedScoreDto;

    }

    private static int findSecondPlayerPosition(int pointWinner) {
        return pointWinner == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }

    private static UpdatedScoreDto updateScoreBasedOnTieBreak(MatchScoreDto matchScoreDto) {

        int gamePointsWinner = matchScoreDto.getGamesByPosition(pointWinner) + POINT;
        int gamePointsNextPlayer = matchScoreDto.getGamesByPosition(nextPlayer);
        int pointsDiff = Math.abs(gamePointsWinner - gamePointsNextPlayer);

        if (isTieBreakWinner(gamePointsWinner, pointsDiff)) {

            updateSetsPoints(matchScoreDto);
            matchScoreDto.setTieBreak(false);

        } else {

            matchScoreDto.setGamesByPosition(pointWinner, gamePointsWinner);
            matchScoreDto.setTieBreak(true);

        }
        return UpdatedScoreDto.builder()
                .updatedScore(matchScoreDto)
                .build();

    }

    private static UpdatedScoreDto updateScorePlayers(MatchScoreDto matchScoreDto) {

        if (hasWinnerAdvantage(matchScoreDto)) {

            addGamePoint(matchScoreDto);

        } else if (hasNextPlayerAdvantage(matchScoreDto)) {

            setDeuce(matchScoreDto);

        } else {

            setPoints(matchScoreDto);

        }

        return setGamesPoints(matchScoreDto);

    }

    private static void setDeuce(MatchScoreDto matchScoreDto){
        matchScoreDto.setPointsByPosition(nextPlayer, FORTY);
    }

    private static void setPoints(MatchScoreDto matchScoreDto) {
        String winnerPoints = updateWinnerPoints(matchScoreDto);

        String nextPlayerPoints = matchScoreDto.getPointsByPosition(nextPlayer);

        if (isAdvantageIn(winnerPoints, nextPlayerPoints)) {

            matchScoreDto.setPointsByPosition(pointWinner, ADVANTAGE);

        } else if (winnerPoints.equals(THREE_POINTS)) {

            matchScoreDto.setPointsByPosition(pointWinner, FORTY);

        } else if (winnerPoints.equals(FOUR_POINTS)) {

            addGamePoint(matchScoreDto);

        } else {

            matchScoreDto.setPointsByPosition(pointWinner, winnerPoints);

        }
    }

    private static String updateWinnerPoints(MatchScoreDto matchScoreDto){
        return Integer.toString(
                Integer.parseInt(
                        matchScoreDto.getPointsByPosition(pointWinner)
                ) + SCORE_POINTS);
    }

    private static boolean isAdvantageIn(String winnerPoints, String nextPlayerPoints){
        return winnerPoints.equals(FOUR_POINTS) && nextPlayerPoints.equals(FORTY);
    }

    private static void addGamePoint(MatchScoreDto matchScoreDto) {

        matchScoreDto.setGamesByPosition(pointWinner, matchScoreDto.getGamesByPosition(pointWinner) + POINT);
        clearPoints(matchScoreDto);

    }

    private static UpdatedScoreDto setGamesPoints(MatchScoreDto matchScoreDto) {
        if (shouldStartTieBreak(matchScoreDto)) {

            matchScoreDto.setTieBreak(true);
            clearPoints(matchScoreDto);
            clearGames(matchScoreDto);

        } else if (ShouldAddSetsPoint(matchScoreDto)) {

            updateSetsPoints(matchScoreDto);
            clearPoints(matchScoreDto);

        }

        return UpdatedScoreDto.builder()
                .updatedScore(matchScoreDto)
                .build();

    }

    private static void updateSetsPoints(MatchScoreDto matchScoreDto) {

        int updatedSetsPoints = matchScoreDto.getSetsByPosition(pointWinner) + POINT;
        matchScoreDto.setSetsByPosition(pointWinner, updatedSetsPoints);
        clearGames(matchScoreDto);

    }

    private static void clearPoints(MatchScoreDto matchScoreDto) {

        matchScoreDto.setPointsByPosition(pointWinner, ZERO_STRING);
        matchScoreDto.setPointsByPosition(nextPlayer, ZERO_STRING);

    }

    private static void clearGames(MatchScoreDto matchScoreDto) {

        matchScoreDto.setGamesByPosition(pointWinner, ZERO_INT);
        matchScoreDto.setGamesByPosition(nextPlayer, ZERO_INT);

    }

    private static boolean hasWinnerAdvantage(MatchScoreDto matchScoreDto) {
        return matchScoreDto.getPointsByPosition(pointWinner).equals(ADVANTAGE);
    }

    private static boolean hasNextPlayerAdvantage(MatchScoreDto matchScoreDto) {
        return matchScoreDto.getPointsByPosition(nextPlayer).equals(ADVANTAGE);
    }

    private static boolean isTieBreakWinner(int gamePointsWinner, int pointsDiff) {
        return gamePointsWinner >= MIN_TIE_BREAK_POINTS && pointsDiff >= MIN_TIE_BREAK_POINTS_DIFF;
    }

    private static boolean ShouldAddSetsPoint(MatchScoreDto matchScoreDto) {
        return matchScoreDto.getGamesByPosition(pointWinner) == MIN_GAMES_POINTS
               && matchScoreDto.getGamesByPosition(nextPlayer) <= MIN_DIFF_FOR_SET_POINT
               || matchScoreDto.getGamesByPosition(pointWinner) > MIN_GAMES_POINTS;
    }

    private static boolean shouldStartTieBreak(MatchScoreDto matchScoreDto) {
        return matchScoreDto.getGamesByPosition(pointWinner) == MIN_GAMES_POINTS
               && matchScoreDto.getGamesByPosition(nextPlayer) == MIN_GAMES_POINTS;
    }

    private static Boolean isWinner(MatchScoreDto matchScoreDto) {
        return matchScoreDto.getSetsByPosition(pointWinner) == SETS_TO_WIN;
    }

}
