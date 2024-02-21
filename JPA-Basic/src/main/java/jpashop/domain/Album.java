package jpashop.domain;

import jakarta.persistence.Entity;

@Entity
public class Album extends Item {
    String artist;
}
