package dev.lqwd.service;

import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.match_score.MatchScoreForUpdateRequestDto;
import dev.lqwd.entity.Player;

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
            matchScoreDtoUpdated.setWinner(setWinner(matchScoreForUpdateDto));
        }

        return matchScoreDtoUpdated;

    }

    private static Player setWinner(MatchScoreDto matchScoreForUpdateDto) {
        if (pointWinner == PLAYER_1) {
            return matchScoreForUpdateDto.getPlayer1();
        }
        return matchScoreForUpdateDto.getPlayer2();
    }

    private static int findSecondPlayerNumber(int pointWinner) {

        return pointWinner == PLAYER_1 ? PLAYER_2 : PLAYER_1;

    }

    private static MatchScoreDto updateScoreBasedOnTieBreak(MatchScoreDto matchScoreForUpdateDto) {

        int winnerPoints = Integer.parseInt(matchScoreForUpdateDto.getScoreByNumber(pointWinner).getPoints()) + POINT;
        int nextPlayerPoints = Integer.parseInt(matchScoreForUpdateDto.getScoreByNumber(nextPlayer).getPoints());
        int pointsDiff = Math.abs(winnerPoints - nextPlayerPoints);

        if (isTieBreakWinner(winnerPoints, pointsDiff)) {

            updateSetsPoints(matchScoreForUpdateDto);
            clearPoints(matchScoreForUpdateDto);

        } else {

            matchScoreForUpdateDto.getScoreByNumber(pointWinner).setPoints(Integer.toString(winnerPoints));

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

        matchScoreForUpdateDto.getScoreByNumber(nextPlayer).setPoints(FORTY);

    }

    private static void setPoints(MatchScoreDto matchScoreForUpdateDto) {
        String winnerPoints = updateWinnerPoints(matchScoreForUpdateDto);

        String nextPlayerPoints = matchScoreForUpdateDto.getScoreByNumber(nextPlayer).getPoints();

        if (isAdvantageIn(winnerPoints, nextPlayerPoints)) {

            matchScoreForUpdateDto.getScoreByNumber(pointWinner).setPoints(ADVANTAGE);

        } else if (winnerPoints.equals(THREE_POINTS)) {

            matchScoreForUpdateDto.getScoreByNumber(pointWinner).setPoints(FORTY);

        } else if (winnerPoints.equals(FOUR_POINTS)) {

            addGamePoint(matchScoreForUpdateDto);

        } else {

            matchScoreForUpdateDto.getScoreByNumber(pointWinner).setPoints(winnerPoints);

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
                        matchScoreForUpdateDto.getScoreByNumber(pointWinner).getPoints()
                ) + SCORE_POINTS);

    }

    private static boolean isAdvantageIn(String winnerPoints, String nextPlayerPoints) {

        return winnerPoints.equals(FOUR_POINTS) && nextPlayerPoints.equals(FORTY);

    }

    private static void addGamePoint(MatchScoreDto matchScoreForUpdateDto) {

        matchScoreForUpdateDto.getScoreByNumber(pointWinner).setGames(matchScoreForUpdateDto.getScoreByNumber(pointWinner).getGames() + POINT);
        clearPoints(matchScoreForUpdateDto);

    }

    private static void updateSetsPoints(MatchScoreDto matchScoreForUpdateDto) {

        int updatedSetsPoints = matchScoreForUpdateDto.getScoreByNumber(pointWinner).getSets() + POINT;
        matchScoreForUpdateDto.getScoreByNumber(pointWinner).setSets(updatedSetsPoints);
        clearGames(matchScoreForUpdateDto);

    }

    private static void clearPoints(MatchScoreDto matchScoreForUpdateDto) {

        matchScoreForUpdateDto.getScoreByNumber(pointWinner).setPoints(ZERO_AS_STRING);
        matchScoreForUpdateDto.getScoreByNumber(nextPlayer).setPoints(ZERO_AS_STRING);

    }

    private static void clearGames(MatchScoreDto matchScoreForUpdateDto) {

        matchScoreForUpdateDto.getScoreByNumber(pointWinner).setGames(ZERO_AS_INT);
        matchScoreForUpdateDto.getScoreByNumber(nextPlayer).setGames(ZERO_AS_INT);
    }

    private static boolean hasWinnerAdvantage(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getScoreByNumber(pointWinner).getPoints().equals(ADVANTAGE);

    }

    private static boolean hasNextPlayerAdvantage(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getScoreByNumber(nextPlayer).getPoints().equals(ADVANTAGE);

    }

    private static boolean isTieBreakWinner(int gamePointsWinner, int pointsDiff) {

        return gamePointsWinner >= MIN_TIE_BREAK_POINTS && pointsDiff >= MIN_TIE_BREAK_POINTS_DIFF;

    }

    private static boolean ShouldAddSetsPoint(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getScoreByNumber(pointWinner).getGames() == MIN_GAMES_POINTS
               && matchScoreForUpdateDto.getScoreByNumber(nextPlayer).getGames() <= MIN_DIFF_FOR_SET_POINT
               || matchScoreForUpdateDto.getScoreByNumber(pointWinner).getGames() > MIN_GAMES_POINTS;
    }

    private static boolean isTieBreak(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getScoreByNumber(pointWinner).getGames() == MIN_GAMES_POINTS
               && matchScoreForUpdateDto.getScoreByNumber(nextPlayer).getGames() == MIN_GAMES_POINTS;

    }

    private static Boolean isWinner(MatchScoreDto matchScoreForUpdateDto) {

        return matchScoreForUpdateDto.getScoreByNumber(pointWinner).getSets() == SETS_TO_WIN;

    }

}
