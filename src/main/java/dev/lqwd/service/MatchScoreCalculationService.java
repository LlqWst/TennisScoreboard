package dev.lqwd.service;

import dev.lqwd.dto.MatchScoreDto;
import dev.lqwd.dto.RequestForScoreUpdateDto;
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

    public UpdatedScoreDto updateScore(RequestForScoreUpdateDto requestForScoreUpdateDto) {

        pointWinner = requestForScoreUpdateDto.getPointWinnerNumber();
        nextPlayer = findSecondPlayerNumber(pointWinner);
        MatchScoreDto matchScoreDto = requestForScoreUpdateDto.getMatchScoreDto();

        UpdatedScoreDto updatedScoreDto;

        if (matchScoreDto.isTieBreak()) {
            updatedScoreDto = updateScoreBasedOnTieBreak(matchScoreDto);
        } else {
            updatedScoreDto = updateScorePlayers(matchScoreDto);
        }

        if (isWinner(matchScoreDto)) {
            updatedScoreDto.setIsWinner(true);
            updatedScoreDto.setWinner(matchScoreDto.getIdByNumber(pointWinner));
        }

        return updatedScoreDto;

    }

    private static int findSecondPlayerNumber(int pointWinner) {

        return pointWinner == PLAYER_1 ? PLAYER_2 : PLAYER_1;

    }

    private static UpdatedScoreDto updateScoreBasedOnTieBreak(MatchScoreDto matchScoreDto) {

        int gamePointsWinner = Integer.parseInt(matchScoreDto.getPointsByNumber(pointWinner)) + POINT;
        int gamePointsNextPlayer = Integer.parseInt(matchScoreDto.getPointsByNumber(nextPlayer));
        int pointsDiff = Math.abs(gamePointsWinner - gamePointsNextPlayer);

        if (isTieBreakWinner(gamePointsWinner, pointsDiff)) {

            updateSetsPoints(matchScoreDto);
            clearPoints(matchScoreDto);
            matchScoreDto.setTieBreak(false);

        } else {

            matchScoreDto.setPointsByNumber(pointWinner, Integer.toString(gamePointsWinner));
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

    private static void setDeuce(MatchScoreDto matchScoreDto) {

        matchScoreDto.setPointsByNumber(nextPlayer, FORTY);

    }

    private static void setPoints(MatchScoreDto matchScoreDto) {
        String winnerPoints = updateWinnerPoints(matchScoreDto);

        String nextPlayerPoints = matchScoreDto.getPointsByNumber(nextPlayer);

        if (isAdvantageIn(winnerPoints, nextPlayerPoints)) {

            matchScoreDto.setPointsByNumber(pointWinner, ADVANTAGE);

        } else if (winnerPoints.equals(THREE_POINTS)) {

            matchScoreDto.setPointsByNumber(pointWinner, FORTY);

        } else if (winnerPoints.equals(FOUR_POINTS)) {

            addGamePoint(matchScoreDto);

        } else {

            matchScoreDto.setPointsByNumber(pointWinner, winnerPoints);

        }
    }

    private static UpdatedScoreDto setGamesPoints(MatchScoreDto matchScoreDto) {

        if (shouldStartTieBreak(matchScoreDto)) {

            matchScoreDto.setTieBreak(true);
            clearPoints(matchScoreDto);

        } else if (ShouldAddSetsPoint(matchScoreDto)) {

            updateSetsPoints(matchScoreDto);
            clearPoints(matchScoreDto);

        }

        return UpdatedScoreDto.builder()
                .updatedScore(matchScoreDto)
                .build();

    }

    private static String updateWinnerPoints(MatchScoreDto matchScoreDto) {

        return Integer.toString(
                Integer.parseInt(
                        matchScoreDto.getPointsByNumber(pointWinner)
                ) + SCORE_POINTS);

    }

    private static boolean isAdvantageIn(String winnerPoints, String nextPlayerPoints) {

        return winnerPoints.equals(FOUR_POINTS) && nextPlayerPoints.equals(FORTY);

    }

    private static void addGamePoint(MatchScoreDto matchScoreDto) {

        matchScoreDto.setGamesByNumber(pointWinner, matchScoreDto.getGamesByNumber(pointWinner) + POINT);
        clearPoints(matchScoreDto);

    }

    private static void updateSetsPoints(MatchScoreDto matchScoreDto) {

        int updatedSetsPoints = matchScoreDto.getSetsByNumber(pointWinner) + POINT;
        matchScoreDto.setSetsByNumber(pointWinner, updatedSetsPoints);
        clearGames(matchScoreDto);

    }

    private static void clearPoints(MatchScoreDto matchScoreDto) {

        matchScoreDto.setPointsByNumber(pointWinner, ZERO_STRING);
        matchScoreDto.setPointsByNumber(nextPlayer, ZERO_STRING);

    }

    private static void clearGames(MatchScoreDto matchScoreDto) {

        matchScoreDto.setGamesByNumber(pointWinner, ZERO_INT);
        matchScoreDto.setGamesByNumber(nextPlayer, ZERO_INT);

    }

    private static boolean hasWinnerAdvantage(MatchScoreDto matchScoreDto) {

        return matchScoreDto.getPointsByNumber(pointWinner).equals(ADVANTAGE);

    }

    private static boolean hasNextPlayerAdvantage(MatchScoreDto matchScoreDto) {

        return matchScoreDto.getPointsByNumber(nextPlayer).equals(ADVANTAGE);

    }

    private static boolean isTieBreakWinner(int gamePointsWinner, int pointsDiff) {

        return gamePointsWinner >= MIN_TIE_BREAK_POINTS && pointsDiff >= MIN_TIE_BREAK_POINTS_DIFF;

    }

    private static boolean ShouldAddSetsPoint(MatchScoreDto matchScoreDto) {

        return matchScoreDto.getGamesByNumber(pointWinner) == MIN_GAMES_POINTS
               && matchScoreDto.getGamesByNumber(nextPlayer) <= MIN_DIFF_FOR_SET_POINT
               || matchScoreDto.getGamesByNumber(pointWinner) > MIN_GAMES_POINTS;

    }

    private static boolean shouldStartTieBreak(MatchScoreDto matchScoreDto) {

        return matchScoreDto.getGamesByNumber(pointWinner) == MIN_GAMES_POINTS
               && matchScoreDto.getGamesByNumber(nextPlayer) == MIN_GAMES_POINTS;

    }

    private static Boolean isWinner(MatchScoreDto matchScoreDto) {

        return matchScoreDto.getSetsByNumber(pointWinner) == SETS_TO_WIN;

    }

}
