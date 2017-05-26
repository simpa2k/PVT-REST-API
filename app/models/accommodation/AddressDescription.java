package models.accommodation;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import models.stringDescriptors.StringDescriptor;
import play.Logger;
import play.libs.Json;
import scala.util.parsing.json.JSONObject;
import services.GoogleService;
import services.TrafikLabService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enver on 2017-05-17.
 */
@Entity

public class AddressDescription extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public long id;

    //private Map<String, Integer> distanceByTypes = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL)
    public List<Distance> distances = new ArrayList<>();

    public List<String> stringDescriptors = new ArrayList<>();

    //@JsonIgnore
    // @OneToOne
//    public Address address;

    //  @OneToOne
    //  public CityDistance cityDistance;
    /*public void addToList(String type, int distance) {
        distanceByTypes.put(type, distance);
    }*/
}