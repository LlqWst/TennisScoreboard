package dev.lqwd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreUpdatedDto {

    MatchScoreDto currentMatch;

    @Builder.Default
    Boolean isThereWinner = false;

    @Builder.Default
    Long winner = 0L;

}
