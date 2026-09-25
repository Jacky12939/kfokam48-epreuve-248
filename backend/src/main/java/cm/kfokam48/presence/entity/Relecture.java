package cm.kfokam48.presence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "relecture",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_relecture_exercice_relecteur",
        columnNames = {"exercice_id", "relecteur_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Relecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // v2 : plus unique seul — jusqu'à 2 relectures par exercice.
    @Column(name = "exercice_id", nullable = false)
    private Long exerciceId;

    @Column(name = "relecteur_id", nullable = false)
    private Long relecteurId;

    @Column
    private Integer note;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "rendu_at")
    private LocalDateTime renduAt;

    public Relecture(Long exerciceId, Long relecteurId) {
        this.exerciceId = exerciceId;
        this.relecteurId = relecteurId;
    }
}
