package dev.lqwd.dto.finished_match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishedMatchRequestDto {

    private int page;
    private String name;

}
