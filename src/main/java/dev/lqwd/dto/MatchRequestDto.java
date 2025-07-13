package dev.lqwd.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchRequestDto {

    private Long player1;
    private Long player2;
    private Long winner;
}
