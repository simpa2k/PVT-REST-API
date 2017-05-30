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

    @OneToMany(cascade = CascadeType.ALL)
    public List<Distance> distances = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<StringDescriptor> stringDescriptors = new ArrayList<>();

    public void addStringDescriptor(StringDescriptor descriptor) {

        descriptor.generateDescription();

        if (descriptor.getDescription() != null) {
            stringDescriptors.add(descriptor);
        }
    }
}