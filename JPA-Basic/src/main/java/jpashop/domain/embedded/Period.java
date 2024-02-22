package jpashop.domain.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Embeddable
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(getStartDate(), period.getStartDate()) && Objects.equals(getEndDate(), period.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartDate(), getEndDate());
    }
}
