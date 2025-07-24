package dev.lqwd.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayersMatchDto {

    private Long player1_id;
    private String player1_name;

    private Long player2_id;
    private String player2_name;
}
