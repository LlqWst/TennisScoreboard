package dev.lqwd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedScoreDto {

    MatchScoreDto updatedScore;

    @Builder.Default
    Boolean isWinner = false;

    @Builder.Default
    Long winner = 0L;

}
