import dev.lqwd.dto.MatchScoreDto;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Экземпляр класса создается один раз, поэтому поля сохраняют значения между тестами:
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // позволяет задавать очередность тестов
public class OngoingMatchesServiceTest {

    private static final Map<UUID, MatchScoreDto> matches = new HashMap<>();
    private  MatchScoreDto currentMatch;
    private  MatchScoreDto currentMatch2;

    private UUID key1;
    private UUID key2;

    @BeforeAll
    void fillData(){
        currentMatch = MatchScoreDto.builder()
                .idPlayer1(1L)
                .idPlayer2(2L)
                .build();

        key1 = UUID.randomUUID();

        currentMatch2 = MatchScoreDto.builder()
                .idPlayer1(2L)
                .idPlayer2(3L)
                .build();
        key2 = UUID.randomUUID();
    }

    @Test
    @Order(1)
    public void addMatchTest(){
        matches.put(key1, currentMatch);
        matches.put(key2, currentMatch2);

        System.out.println("addMatchTest: " + matches);
    }

    @Test
    @Order(2)
    public void removeMatchTest(){
        System.out.println(matches);

        matches.remove(key1);
        System.out.println("removeMatchTest: " + matches);

    }

    @Test
    @Order(3)
    public void getMatchTest(){
        System.out.println("getMatchTest: " + matches.get(key2));

    }
}
