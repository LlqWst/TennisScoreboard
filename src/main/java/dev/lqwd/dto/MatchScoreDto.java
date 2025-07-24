package dev.lqwd.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchScoreDto {

    private Long idPlayer1;
    private Long idPlayer2;

    @Builder.Default
    private int score1 = 0;

    @Builder.Default
    private int score2 = 0;


}
