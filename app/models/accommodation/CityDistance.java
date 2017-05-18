package models.accommodation;

import com.avaje.ebean.Model;
import services.TrafikLabService;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Enver on 2017-05-17.
 */
@Entity
public class CityDistance extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public int duration ;

    public CityDistance(int duration){


        this.duration = duration;

    }





}