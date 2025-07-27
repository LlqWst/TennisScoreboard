package dev.lqwd.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreForUpdatingDto {

    MatchScoreDto matchScoreDto;

    int pointWinnerPosition;

}
