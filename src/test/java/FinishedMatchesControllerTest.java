import dev.lqwd.FinishedMatchMapper;
import dev.lqwd.dto.finished_match.FinishedMatchResponseDto;
import dev.lqwd.dto.finished_match.FinishedMatchRequestDto;
import dev.lqwd.entity.Match;
import dev.lqwd.entity.Player;
import dev.lqwd.service.FinishedMatchesService;
import org.junit.jupiter.api.Test;

import java.util.List;


public class FinishedMatchesControllerTest {

    private static final FinishedMatchesService FINISHED_MATCHES_SERVICE = new FinishedMatchesService();
    private static final FinishedMatchMapper mapper = FinishedMatchMapper.INSTANCE;

    @Test
    public void doGetTest() {

        String pageParameter = "123";
        String name = "ivan";

        int page = 1;

        if (pageParameter != null) {
            page = Integer.parseInt(pageParameter);
        }

        FinishedMatchRequestDto finishedMatchRequestDto = FinishedMatchRequestDto.builder()
                .page(page)
                .name(name)
                .build();

        int maxPages = 4;
        List <Match> matches = List.of(Match.builder()
                .id(1L)
                .player1(Player.builder()
                        .id(1L)
                        .build())
                .player2(Player.builder()
                        .id(2L)
                        .build())
                .winner(Player.builder()
                        .id(1L)
                        .build())
                .build()
        );

        List<FinishedMatchResponseDto> finishedMatchesResponseDto = matches
                .stream()
                .map(mapper::toMatchResponseDto)
                .toList();
        System.out.println(finishedMatchesResponseDto);
    }

}
