package dev.lqwd.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreForUpdatingDto {

    MatchScoreDto currentMatch;
    Long pointForId;

}
