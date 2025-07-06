package dev.lqwd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchRequestDto {

    private Long player1;
    private Long player2;
    private Long winner;
}
