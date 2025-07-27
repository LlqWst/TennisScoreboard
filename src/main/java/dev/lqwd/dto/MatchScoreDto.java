package dev.lqwd.dto;

import dev.lqwd.exception.BadRequestException;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchScoreDto {

    private Long idPlayer1;

    private Long idPlayer2;

    @Builder.Default
    private boolean isTieBreak = false;

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

    public String getPointsByPosition(int position) {

        if (position == 1) {
            return points1;
        } else if (position == 2) {
            return points2;
        } else {
            throw new RuntimeException();
        }

    }

    public void setPointsByPosition(int position, String value) {

        if (position == 1) {
            this.points1 = value;
        } else if (position == 2) {
            this.points2 = value;
        } else {
            throw new RuntimeException();
        }

    }

    public int getGamesByPosition(int position) {

        if (position == 1) {
            return games1;
        } else if (position == 2) {
            return games2;
        } else {
            throw new RuntimeException();
        }


    }

    public void setGamesByPosition(int position, int value) {

        if (position == 1) {
            this.games1 = value;
        } else if (position == 2) {
            this.games2 = value;
        } else {
            throw new RuntimeException();
        }

    }

    public int getSetsByPosition(int position) {

        if (position == 1) {
            return sets1;
        } else if (position == 2) {
            return sets2;
        } else {
            throw new RuntimeException();
        }


    }

    public void setSetsByPosition(int position, int value) {

        if (position == 1) {
            this.sets1 = value;
        } else if (position == 2) {
            this.sets2 = value;
        } else {
            throw new RuntimeException();
        }

    }

}
