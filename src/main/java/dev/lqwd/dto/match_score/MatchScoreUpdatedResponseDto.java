package dev.lqwd.dto.match_score;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchScoreUpdatedResponseDto {

    MatchScoreDto matchScoreDto;

    String player1Name;

    String player2Name;

    int numberPlayer1;

    int numberPlayer2;

}
