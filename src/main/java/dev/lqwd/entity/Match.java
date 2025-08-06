package dev.lqwd.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NonNull
    private Player player1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player2")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NonNull
    private Player player2;

    @ManyToOne(optional = false)
    @JoinColumn(name = "winner")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NonNull
    private Player winner;
}
