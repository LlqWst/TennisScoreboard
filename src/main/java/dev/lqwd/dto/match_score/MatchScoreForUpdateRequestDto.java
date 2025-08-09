package dev.lqwd.dto.match_score;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchScoreForUpdateRequestDto {

    MatchScoreDto matchScoreDto;

    Long pointWinnerId;

}
