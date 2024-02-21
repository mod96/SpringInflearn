package jpashop.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Movie")
public class Movie extends Item {
    String director;
    String actor;
}
