package models.stringDescriptors;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Address;
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
@DiscriminatorValue("STRING_DESCRIPTOR")
public abstract class StringDescriptor extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Transient
    public JsonNode node;

    public String description;

    @Transient
    public String[] possibleDescriptions;

    public StringDescriptor(JsonNode node, String[] possibleDescriptions) {

        this.node = node;
        this.possibleDescriptions = possibleDescriptions;

    }

    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return possibleDescriptions[random.nextInt(3)];
    }


    public abstract String generateDescription();

}

