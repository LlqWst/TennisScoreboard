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

    private static final String ADVANTAGE = "Ad";

    @InjectMocks
    private MatchScoreCalculationService service;


    @Test
    void should_ReturnAdvantage_When_Player1WinsPointAtDeuce(){
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(1L)
                                .idPlayer2(2L)
                                .points1("40")
                                .points2("40")
                                .games1(0)
                                .games2(0)
                                .sets1(0)
                                .sets2(0)
                                .build())
                        .pointWinnerNumber(1)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(1L)
                .idPlayer2(2L)
                .idWinner(null)
                .points1(ADVANTAGE)
                .points2("40")
                .games1(0)
                .games2(0)
                .sets1(0)
                .sets2(0)
                .build();

        MatchScoreDto actual = service.calculateScore(request);

        assertEquals(expected, actual, "Test Advantage for player1: FAILED");
        System.out.println("Test Advantage for player1: OK");

    }

    @Test
    void should_ReturnAdvantage_When_Player2WinsPointAtDeuce(){
        MatchScoreForUpdateRequestDto request =
                MatchScoreForUpdateRequestDto.builder()
                        .matchScoreDto(MatchScoreDto.builder()
                                .idPlayer1(1L)
                                .idPlayer2(2L)
                                .points1("40")
                                .points2("40")
                                .games1(0)
                                .games2(0)
                                .sets1(0)
                                .sets2(0)
                                .build())
                        .pointWinnerNumber(2)
                        .build();

        MatchScoreDto expected = MatchScoreDto.builder()
                .idPlayer1(1L)
                .idPlayer2(2L)
                .idWinner(null)
                .points1("40")
                .points2(ADVANTAGE)
                .games1(0)
                .games2(0)
                .sets1(0)
                .sets2(0)
                .build();

        MatchScoreDto actual = service.calculateScore(request);

        assertEquals(expected, actual, "Test Advantage for player2: FAILED");
        System.out.println("Test Advantage for player2: OK");

    }

}
