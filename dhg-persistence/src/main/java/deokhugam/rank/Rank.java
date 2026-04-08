package deokhugam.rank;

import deokhugam.rank.enums.RankTarget;
import deokhugam.rank.enums.PeriodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rank_snapshot")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RankTarget target_type;

    private Long target_id;

    @Enumerated(EnumType.STRING)
    private PeriodType period_type;

    private LocalDate period_start;

    private LocalDate period_end;

    private Integer rank_no;

    private BigDecimal score;

    private LocalDateTime created_at;
}
