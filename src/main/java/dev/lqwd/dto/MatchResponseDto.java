package dev.lqwd.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponseDto {

    private Long id;
    @NonNull
    private Long player1;
    @NonNull
    private Long player2;
    @NonNull
    private Long winner;
}
