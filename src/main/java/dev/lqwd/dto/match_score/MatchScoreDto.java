package dev.lqwd.dto.match_score;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MatchScoreDto {

    private Long idPlayer1;

    private Long idPlayer2;

    private Long idWinner;


    @Builder.Default
    private int sets1 = 0;

    @Builder.Default
    private int sets2 = 0;

    @Builder.Default
    private int games1 = 0;

    @Builder.Default
    private int games2 = 0;

    @Builder.Default
    private String points1 = "0";

    @Builder.Default
    private String points2 = "0";

    public long getIdByNumber(int number) {

        if (number == 1) {
            return idPlayer1;
        } else if (number == 2) {
            return idPlayer2;
        } else {
            throw new RuntimeException();
        }

    }

    public String getPointsByNumber(int number) {

        if (number == 1) {
            return points1;
        } else if (number == 2) {
            return points2;
        } else {
            throw new RuntimeException();
        }

    }

    public void setPointsByNumber(int number, String value) {

        if (number == 1) {
            this.points1 = value;
        } else if (number == 2) {
            this.points2 = value;
        } else {
            throw new RuntimeException();
        }

    }

    public int getGamesByNumber(int number) {

        if (number == 1) {
            return games1;
        } else if (number == 2) {
            return games2;
        } else {
            throw new RuntimeException();
        }

    }

    public void setGamesByNumber(int number, int value) {

        if (number == 1) {
            this.games1 = value;
        } else if (number == 2) {
            this.games2 = value;
        } else {
            throw new RuntimeException();
        }

    }

    public int getSetsByNumber(int number) {

        if (number == 1) {
            return sets1;
        } else if (number == 2) {
            return sets2;
        } else {
            throw new RuntimeException();
        }

    }

    public void setSetsByNumber(int number, int value) {

        if (number == 1) {
            this.sets1 = value;
        } else if (number == 2) {
            this.sets2 = value;
        } else {
            throw new RuntimeException();
        }

    }

}
