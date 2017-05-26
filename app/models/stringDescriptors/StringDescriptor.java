package models.stringDescriptors;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Address;
import models.accommodation.AddressDescription;
import play.Configuration;
import play.Logger;
import services.GoogleService;

import javax.inject.Inject;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("STRING")
public abstract class StringDescriptor extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public long id;

    @JsonIgnore
    @Transient
    public JsonNode node;

    @JsonIgnore
    @Transient
    public String name;

    public String description;

    @JsonIgnore
    @ManyToOne
    public AddressDescription addressDescription;

    @JsonIgnore
    @Transient
    public String[] possibleDescriptions;

    public StringDescriptor(JsonNode node, String[] possibleDescriptions) {

        if (node.size() == 0) {
            throw new IllegalArgumentException("Empty JsonNode passed to StringDescriptor constructor. A descriptor must have data to work with.");
        }

        this.node = node;
        this.possibleDescriptions = possibleDescriptions;

        this.name = node.findValue("name").asText();

    }

    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return possibleDescriptions[random.nextInt(3)];
    }


    public abstract String generateDescription();

}

