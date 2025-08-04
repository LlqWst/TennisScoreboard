package dev.lqwd.dto.match_score;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Score {

    @Builder.Default
    private String points = "0";

    @Builder.Default
    private int games = 0;

    @Builder.Default
    private int sets = 0;

}
