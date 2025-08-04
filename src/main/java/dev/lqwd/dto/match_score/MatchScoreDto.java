package dev.lqwd.dto.match_score;

import dev.lqwd.entity.Player;
import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MatchScoreDto {

    private Player player1;

    private Player player2;

    private Player winner;

    @Builder.Default
    private Score score1 = new Score();

    @Builder.Default
    private Score score2 = new Score();

    public Score getScoreByNumber(int number) {

        if (number == 1) {
            return score1;
        } else if (number == 2) {
            return score2;
        } else {
            throw new RuntimeException();
        }

    }

}
