package dev.lqwd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponseDto {

    private int firstPage;

    private int lastPage;

    private int prevList;

    private int nextList;

}
