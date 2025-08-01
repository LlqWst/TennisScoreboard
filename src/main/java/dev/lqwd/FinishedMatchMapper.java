package dev.lqwd;

import dev.lqwd.dto.finished_match.FinishedMatchResponseDto;
import dev.lqwd.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FinishedMatchMapper {

    FinishedMatchMapper INSTANCE = Mappers.getMapper(FinishedMatchMapper.class);

    FinishedMatchResponseDto toMatchResponseDto(Match match);

    Match toMatch(FinishedMatchResponseDto finishedMatchedResponseDto);

}
