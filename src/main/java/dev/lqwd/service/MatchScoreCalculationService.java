package dev.lqwd.service;

import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.match_score.MatchScoreForUpdateRequestDto;

public class MatchScoreCalculationService {

    private static final String ZERO_AS_STRING = "0";
    private static final String ADVANTAGE = "Ad";
    private static final String FORTY = "40";
    private static final String THREE_POINTS = "45";
    private static final String FOUR_POINTS = "55";
    private static final int ZERO_AS_INT = 0;
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

    public MatchScoreDto calculateScore(MatchScoreForUpdateRequestDto matchScoreForUpdateRequestDto) {

        pointWinner = matchScoreForUpdateRequestDto.getPointWinnerNumber();
        nextPlayer = findSecondPlayerNumber(pointWinner);
        MatchScoreDto matchScoreForUpdateDto = matchScoreForUpdateRequestDto.getMatchScoreDto();

        MatchScoreDto matchScoreDtoUpdated;

        if (isTieBreak(matchScoreForUpdateDto)) {
            matchScoreDtoUpdated = updateScoreBasedOnTieBreak(matchScoreForUpdateDto);
        } else {
            matchScoreDtoUpdated = updateScorePlayers(matchScoreForUpdateDto);
        }

        if (isWinner(matchScoreForUpdateDto)) {
            matchScoreDtoUpdated.setIdWinner(matchScoreForUpdateDto.getIdByNumber(pointWinner));
        }

        return matchScoreDtoUpdated;

    }

    private static int findSecondPlayerNumber(int pointWinner) {

        return pointWinner == PLAYER_1 ? PLAYER_2 : PLAYER_1;

    }

    private static MatchScoreDto updateScoreBasedOnTieBreak(MatchScoreDto matchScoreForUpdateDto) {

        int gamePointsWinner = Integer.parseInt(matchScoreForUpdateDto.getPointsByNumber(pointWinner)) + POINT;
        int gamePointsNextPlayer = Integer.parseInt(matchScoreForUpdateDto.getPointsByNumber(nextPlayer));
        int pointsDiff = Math.abs(gamePointsWinner - gamePointsNextPlayer);

        if (isTieBreakWinner(gamePointsWinner, pointsDiff)) {

            updateSetsPoints(matchScoreForUpdateDto);
            clearPoints(matchScoreForUpdateDto);

        } else {

            matchScoreForUpdateDto.setPointsByNumber(pointWinner, Integer.toString(gamePointsWinner));

        }
        return matchScoreForUpdateDto;

    }

    private static MatchScoreDto updateScorePlayers(MatchScoreDto matchScoreForUpdateDto) {

        if (hasWinnerAdvantage(matchScoreForUpdateDto)) {

            addGamePoint(matchScoreForUpdateDto);

        } else if (hasNextPlayerAdvantage(matchScoreForUpdateDto)) {

            setDeuce(matchScoreForUpdateDto);

        } else {

            setPoints(matchScoreForUpdateDto);

        }

        return setGamesPoints(matchScoreForUpdateDto);

    }

    private static void setDeuce(MatchScoreDto matchScoreForUpdateDto) {

        matchScoreForUpdateDto.setPointsByNumber(nextPlayer, FORTY);

    }

    private static void setPoints(MatchScoreDto matchScoreForUpdateDto) {
        String winnerPoints = updateWinnerPoints(matchScoreForUpdateDto);

        String nextPlayerPoints = matchScoreForUpdateDto.getPointsByNumber(nextPlayer);

        if (isAdvantageIn(winnerPoints, nextPlayerPoints)) {

            matchScoreForUpdateDto.setPointsByNumber(pointWinner, ADVANTAGE);

        } else if (winnerPoints.equals(THREE_POINTS)) {

            matchScoreForUpdateDto.setPointsByNumber(pointWinner, FORTY);

        } else if (winnerPoints.equals(FOUR_POINTS)) {

            addGamePoint(matchScoreForUpdateDto);

        } else {

            matchScoreForUpdateDto.setPointsByNumber(pointWinner, winnerPoints);

        }
    }

    private static MatchScoreDto setGamesPoints(MatchScoreDto matchScoreForUpdateDto) {

        if (isTieBreak(matchScoreForUpdateDto)) {

            clearPoints(matchScoreForUpdateDto);

        } else if (ShouldAddSetsPoint(matchScoreForUpdateDto)) {

            updateSetsPoints(matchScoreForUpdateDto);
            clearPoints(matchScoreForUpdateDto);

        }

        return matchScoreForUpdateDto;

    }

    private static String updateWinnerPoints(MatchScoreDto matchScoreForUpdateDto) {

        return Integer.toString(
                Integer.parseInt(
                        matchScoreForUpdateDto.getPointsByNumber(pointWinner)
                ) + SCORE_POINTS);

    }

    private static boolean isAdvantageIn(String winnerPoints, String nextPlayerPoints) {

        return winnerPoints.equals(FOUR_POINTS) && nextPlayerPoints.equals(FORTY);

    }

    private static void addGamePoint(MatchScoreDto matchScoreForUpdateDto) {

        matchScoreForUpdateDto.setGamesByNumber(pointWinner, matchScoreForUpdateDto.getGamesByNumber(pointWinner) + POINT);
        clearPoints(matchScoreForUpdateDto);

    }

    private static void updateSetsPoints(MatchScoreDto matchScoreForUpdateDto) {

        int updatedSetsPoints = matchScoreForUpdateDto.getSetsByNumber(pointWinner) + POINT;
        matchScoreForUpdateDto.setSetsByNumber(pointWinner, updatedSetsPoints);
        clearGames(matchScoreForUpdateDto);

    }

    private static void clearPoints(MatchScoreDto matchScoreForUpdateDto) {

        matchScoreForUpdateDto.setPointsByNumber(pointWinner, ZERO_AS_STRING);
        matchScoreForUpdateDto.setPointsByNumber(nextPlayer, ZERO_AS_STRING);

    }

    private static void clearGames(MatchScoreDto matchScoreForUpdateDto) {

        matchScoreForUpdateDto.setGamesByNumber(pointWinner, ZERO_AS_INT);
        matchScoreForUpdateDto.setGamesByNumber(nextPlayer, ZERO_AS_INT);

    }

    private static boolean hasWinnerAdvantage(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getPointsByNumber(pointWinner).equals(ADVANTAGE);

    }

    private static boolean hasNextPlayerAdvantage(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getPointsByNumber(nextPlayer).equals(ADVANTAGE);

    }

    private static boolean isTieBreakWinner(int gamePointsWinner, int pointsDiff) {

        return gamePointsWinner >= MIN_TIE_BREAK_POINTS && pointsDiff >= MIN_TIE_BREAK_POINTS_DIFF;

    }

    private static boolean ShouldAddSetsPoint(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getGamesByNumber(pointWinner) == MIN_GAMES_POINTS
               && matchScoreForUpdateDto.getGamesByNumber(nextPlayer) <= MIN_DIFF_FOR_SET_POINT
               || matchScoreForUpdateDto.getGamesByNumber(pointWinner) > MIN_GAMES_POINTS;

    }

    private static boolean isTieBreak(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getGamesByNumber(pointWinner) == MIN_GAMES_POINTS
               && matchScoreForUpdateDto.getGamesByNumber(nextPlayer) == MIN_GAMES_POINTS;

    }

    private static Boolean isWinner(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getSetsByNumber(pointWinner) == SETS_TO_WIN;

    }

}
