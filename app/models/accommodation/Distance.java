package models.accommodation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Distance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public long id;

    @Column(nullable = false)
    public String distanceName;

    public Integer meters;
    public Integer duration;

    public Distance(String distanceName, Integer meters, Integer duration) {

        this.distanceName = distanceName;
        this.meters = meters;
        this.duration = duration;

    }
}
