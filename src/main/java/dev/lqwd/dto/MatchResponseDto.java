package dev.lqwd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
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
