package dev.lqwd.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "Matches", schema = "tennisScoreboard")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(optional = false) // not null constraint поэтому будет inner join
    @JoinColumn(name = "player1")
    private Player player1;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "player2")
    private Player player2;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "winner")
    private Player winner;
}
