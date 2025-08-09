import dev.lqwd.dto.match_score.MatchScoreDto;
import dev.lqwd.dto.match_score.MatchScoreForUpdateRequestDto;
import dev.lqwd.dto.match_score.Score;
import dev.lqwd.entity.Player;
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
    private static final int MIN_POINTS_FOR_GAME = 4;
    private static final int SETS_TO_WIN = 2;

    @InjectMocks
    private MatchScoreCalculationService service;


    @Test
    void should_ReturnGame_When_PlayerWins4Points() {
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(ZERO_AS_INT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(ZERO_AS_INT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(POINT)
                                .sets(ZERO_AS_INT)
                                .build())
                        .score2(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(FORTY)
                                        .games(ZERO_AS_INT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(FORTY)
                                        .games(ZERO_AS_INT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(ADVANTAGE)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
                        .score2(Score.builder()
                                .points(FORTY)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(ADVANTAGE)
                                        .games(ZERO_AS_INT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(FORTY)
                                        .games(ZERO_AS_INT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(POINT)
                                .sets(ZERO_AS_INT)
                                .build())
                        .score2(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(FORTY)
                                        .games(ZERO_AS_INT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(ADVANTAGE)
                                        .games(ZERO_AS_INT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(FORTY)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
                        .score2(Score.builder()
                                .points(FORTY)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(MIN_GAMES_POINTS)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(MIN_GAMES_POINTS)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(String.valueOf(POINT))
                                .games(MIN_GAMES_POINTS)
                                .sets(ZERO_AS_INT)
                                .build())
                        .score2(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(MIN_GAMES_POINTS)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(FORTY)
                                        .games(5)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(ONE_POINT_AS_SCORE)
                                        .games(MIN_GAMES_POINTS)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(String.valueOf(POINT))
                                .games(MIN_GAMES_POINTS)
                                .sets(ZERO_AS_INT)
                                .build())
                        .score2(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(MIN_GAMES_POINTS)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(MIN_GAMES_POINTS)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(MIN_GAMES_POINTS)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(POINT)
                                .build())
                        .score2(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(MIN_GAMES_POINTS)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(String.valueOf(MIN_GAMES_POINTS))
                                        .games(MIN_GAMES_POINTS)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(POINT)
                                .build())
                        .score2(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(FORTY)
                                        .games(5)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(4)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(POINT)
                                .build())
                        .score2(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(FORTY)
                                        .games(MIN_GAMES_POINTS - POINT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .score2(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(MIN_GAMES_POINTS - POINT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(null)
                        .score1(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(POINT)
                                .build())
                        .score2(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
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
                                .player1(Player.builder()
                                        .id(PLAYER_1_ID)
                                        .build())
                                .player2(Player.builder()
                                        .id(PLAYER_2_ID)
                                        .build())
                                .winner(null)
                                .score1(Score.builder()
                                        .points(FORTY)
                                        .games(MIN_GAMES_POINTS - POINT)
                                        .sets(POINT)
                                        .build())
                                .score2(Score.builder()
                                        .points(ZERO_AS_STRING)
                                        .games(ZERO_AS_INT)
                                        .sets(ZERO_AS_INT)
                                        .build())
                                .build())
                        .pointWinnerId(PLAYER_1_ID)
                        .build();

        MatchScoreDto expected =
                MatchScoreDto.builder()
                        .player1(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .player2(Player.builder()
                                .id(PLAYER_2_ID)
                                .build())
                        .winner(Player.builder()
                                .id(PLAYER_1_ID)
                                .build())
                        .score1(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(SETS_TO_WIN)
                                .build())
                        .score2(Score.builder()
                                .points(ZERO_AS_STRING)
                                .games(ZERO_AS_INT)
                                .sets(ZERO_AS_INT)
                                .build())
                        .build();

        MatchScoreDto actual = service.calculateScore(request);

        assertEquals(expected, actual, "Test return winner id when get 2 sets // for player1: FAILED");
        System.out.println("Test return winner id when get 2 sets // for player1: PASSED");

    }

}

