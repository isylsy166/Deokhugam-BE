package deokhugam.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "social_account",
        uniqueConstraints = {@UniqueConstraint(
            name = "uq_social_account_provider_provider_user_id",
            columnNames = {"provider", "provider_user_id"}
        )}
)
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String provider;

    private String provider_user_id;
}
