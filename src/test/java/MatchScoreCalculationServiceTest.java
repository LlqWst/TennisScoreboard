import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.match_score.MatchScoreForUpdateRequestDto;
import dev.lqwd.service.MatchScoreCalculationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MatchScoreCalculationServiceTest {

    private static final String ZERO_AS_STRING = "0";
    private static final String ADVANTAGE = "Ad";
    private static final String FORTY = "40";
    private static final String ONE_POINT_AS_SCORE = "15";
    private static final int ZERO_AS_INT = 0;
    private static final int POINT = 1;
    private static final int MIN_GAMES_POINTS = 6;
    private static final int MIN_TIE_BREAK_POINTS = 7;
    private static final long PLAYER_1_ID = 1L;
    private static final long PLAYER_2_ID = 2L;
    private static final int PLAYER_1_NUMBER = 1;
    private static final int MIN_POINTS_FOR_GAME = 4;
    private static final int SETS_TO_WIN = 2;

    @InjectMocks
    private MatchScoreCalculationService service;


    @Test
    void should_ReturnGame_When_PlayerWins4Points() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(ZERO_AS_STRING)
                                .points2(ZERO_AS_STRING)
                                .games1(ZERO_AS_INT)
                                .games2(ZERO_AS_INT)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(ZERO_AS_STRING)
                .points2(ZERO_AS_STRING)
                .games1(POINT)
                .games2(ZERO_AS_INT)
                .sets1(ZERO_AS_INT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = null;

        for (int i = 1; i <= MIN_POINTS_FOR_GAME; i++) {
            actual = service.calculateScore(request);
            request.setMatchScoreDto(actual);
        }

        assertEquals(expected, actual, "Test get Game point after 4 points // player1 should win game: FAILED");
        System.out.println("Test get Game point after 4 points // player1 should win game: PASSED");

    }

    @Test
    void should_ReturnAdvantage_When_PlayerWinsPointAtDeuce() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(FORTY)
                                .points2(FORTY)
                                .games1(ZERO_AS_INT)
                                .games2(ZERO_AS_INT)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(ADVANTAGE)
                .points2(FORTY)
                .games1(ZERO_AS_INT)
                .games2(ZERO_AS_INT)
                .sets1(ZERO_AS_INT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = service.calculateScore(request);

        assertEquals(expected, actual, "Test return Advantage // for player1: FAILED");
        System.out.println("Test return Advantage // for player1: PASSED");

    }

    @Test
    void should_ReturnGame_When_PlayerWinsPointAtAdvantage() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(ADVANTAGE)
                                .points2(FORTY)
                                .games1(ZERO_AS_INT)
                                .games2(ZERO_AS_INT)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(ZERO_AS_STRING)
                .points2(ZERO_AS_STRING)
                .games1(POINT)
                .games2(ZERO_AS_INT)
                .sets1(ZERO_AS_INT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = service.calculateScore(request);

        assertEquals(expected, actual, "Test return GamePoint after advantage // for player1: FAILED");
        System.out.println("Test return GamePoint after advantage // for player1: PASSED");

    }

    @Test
    void should_ReturnDeuce_When_PlayerWinsPointAtNextPlayerHasAdvantage() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(FORTY)
                                .points2(ADVANTAGE)
                                .games1(ZERO_AS_INT)
                                .games2(ZERO_AS_INT)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(FORTY)
                .points2(FORTY)
                .games1(ZERO_AS_INT)
                .games2(ZERO_AS_INT)
                .sets1(ZERO_AS_INT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = service.calculateScore(request);

        assertEquals(expected, actual, "Test back to Deuce after player2 lost advantage // for player1: FAILED");
        System.out.println("Test back to Deuce after player2 lost advantage // for player1: PASSED");

    }

    @Test
    void should_ReturnPoint_When_PlayerWinsPointAtTieBreak() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(ZERO_AS_STRING)
                                .points2(ZERO_AS_STRING)
                                .games1(MIN_GAMES_POINTS)
                                .games2(MIN_GAMES_POINTS)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(String.valueOf(POINT))
                .points2(ZERO_AS_STRING)
                .games1(MIN_GAMES_POINTS)
                .games2(MIN_GAMES_POINTS)
                .sets1(ZERO_AS_INT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = service.calculateScore(request);

        assertEquals(expected, actual, "Test TieBreak point // for player1: FAILED");
        System.out.println("Test TieBreak point // for player1: PASSED");

    }

    @Test
    void should_ReturnPoint_When_PlayerStartTieBrakeAndGetOneMorePoint() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(FORTY)
                                .points2(ONE_POINT_AS_SCORE)
                                .games1(5)
                                .games2(MIN_GAMES_POINTS)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(String.valueOf(POINT))
                .points2(ZERO_AS_STRING)
                .games1(MIN_GAMES_POINTS)
                .games2(MIN_GAMES_POINTS)
                .sets1(ZERO_AS_INT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = null;
        for (int i = 1; i <= 2; i++) {
            actual = service.calculateScore(request);
            request.setMatchScoreDto(actual);
        }

        assertEquals(expected, actual, "Test player with 5 gamePoint and get 2 point should have 1 point // for player1: FAILED");
        System.out.println("Test player with 5 gamePoint and get 2 point should have 1 point // for player1: PASSED");

    }

    @Test
    void should_ReturnSet_When_PlayerWinsTieBreakPointAfterPoint() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(ZERO_AS_STRING)
                                .points2(ZERO_AS_STRING)
                                .games1(MIN_GAMES_POINTS)
                                .games2(MIN_GAMES_POINTS)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(ZERO_AS_STRING)
                .points2(ZERO_AS_STRING)
                .games1(ZERO_AS_INT)
                .games2(ZERO_AS_INT)
                .sets1(POINT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = null;
        for (int i = 1; i <= MIN_TIE_BREAK_POINTS; i++) {
            actual = service.calculateScore(request);
            request.setMatchScoreDto(actual);
        }

        assertEquals(expected, actual, "Test TieBreak point after point // player1 should win set: FAILED");
        System.out.println("Test TieBreak point after point // player1 should win set: PASSED");

    }

    @Test
    void should_ReturnSet_When_PlayerWinsTieBreakWith8Points() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(ZERO_AS_STRING)
                                .points2(String.valueOf(MIN_GAMES_POINTS))
                                .games1(MIN_GAMES_POINTS)
                                .games2(MIN_GAMES_POINTS)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(ZERO_AS_STRING)
                .points2(ZERO_AS_STRING)
                .games1(ZERO_AS_INT)
                .games2(ZERO_AS_INT)
                .sets1(POINT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = null;
        for (int i = 1; i <= MIN_TIE_BREAK_POINTS + 1; i++) {
            actual = service.calculateScore(request);
            request.setMatchScoreDto(actual);
        }

        assertEquals(expected, actual, "Test TieBreak // player1 should win set with 8 points: FAILED");
        System.out.println("Test TieBreak // player1 should win set with 8 points: PASSED");

    }

    @Test
    void should_ReturnSet_When_PlayerWins6GamePoints() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(FORTY)
                                .points2(ZERO_AS_STRING)
                                .games1(5)
                                .games2(4)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(ZERO_AS_STRING)
                .points2(ZERO_AS_STRING)
                .games1(ZERO_AS_INT)
                .games2(ZERO_AS_INT)
                .sets1(POINT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = service.calculateScore(request);

        assertEquals(expected, actual, "Test player win set with 6 games // for player1: FAILED");
        System.out.println("Test player win set with 6 games // for player1: PASSED");

    }

    @Test
    void should_ReturnSet_When_PlayerWins7GamePoints() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(FORTY)
                                .points2(ZERO_AS_STRING)
                                .games1(5)
                                .games2(5)
                                .sets1(ZERO_AS_INT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(null)
                .points1(ZERO_AS_STRING)
                .points2(ZERO_AS_STRING)
                .games1(ZERO_AS_INT)
                .games2(ZERO_AS_INT)
                .sets1(POINT)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = null;

        for (int i = 1; i <= MIN_POINTS_FOR_GAME + POINT; i++){
            actual = service.calculateScore(request);
            request.setMatchScoreDto(actual);
        }

        assertEquals(expected, actual, "Test player win set with 7 games, next player starts with 5 game points// for player1: FAILED");
        System.out.println("Test player win set with 7 games, next player starts with 5 game points// for player1: PASSED");

    }

    @Test
    void should_ReturnWinnerId_When_PlayerWins2Sets() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(PLAYER_1_ID)
                                .idPlayer2(PLAYER_2_ID)
                                .points1(FORTY)
                                .points2(ZERO_AS_STRING)
                                .games1(5)
                                .games2(ZERO_AS_INT)
                                .sets1(POINT)
                                .sets2(ZERO_AS_INT)
                                .build())
                        .pointWinnerNumber(PLAYER_1_NUMBER)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(PLAYER_1_ID)
                .idPlayer2(PLAYER_2_ID)
                .idWinner(PLAYER_1_ID)
                .points1(ZERO_AS_STRING)
                .points2(ZERO_AS_STRING)
                .games1(ZERO_AS_INT)
                .games2(ZERO_AS_INT)
                .sets1(SETS_TO_WIN)
                .sets2(ZERO_AS_INT)
                .build();

        MatchScoreDto actual = service.calculateScore(request);

        assertEquals(expected, actual, "Test return winner id when get 2 sets // for player1: FAILED");
        System.out.println("Test return winner id when get 2 sets // for player1: PASSED");

    }

}

