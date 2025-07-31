package dev.lqwd.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestForScoreUpdateDto {

    MatchScoreDto matchScoreDto;

    int pointWinnerNumber;

}
