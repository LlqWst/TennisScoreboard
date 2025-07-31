package dev.lqwd.dto;

import dev.lqwd.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishedMatchResponseDto {

    Player player1;

    Player player2;

    Player winner;

}
