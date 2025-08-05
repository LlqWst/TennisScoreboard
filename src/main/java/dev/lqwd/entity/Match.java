package dev.lqwd.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player1")
    @NonNull
    private Player player1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player2")
    @NonNull
    private Player player2;

    @ManyToOne(optional = false)
    @JoinColumn(name = "winner")
    @NonNull
    private Player winner;
}
