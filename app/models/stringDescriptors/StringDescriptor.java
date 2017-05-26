package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Address;
import play.Configuration;
import play.Logger;
import services.GoogleService;

import javax.inject.Inject;
import java.util.ArrayList;


/**
 * Created by Henke on 2017-05-23.
 */

public interface StringDescriptor {

    String getDescription();

}

