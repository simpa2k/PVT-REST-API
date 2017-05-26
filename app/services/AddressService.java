package services;

import com.fasterxml.jackson.databind.JsonNode;
import models.accommodation.Address;
import models.accommodation.AddressDescription;
import models.accommodation.Distance;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;

/**
 * @author Simon Olofsson
 */
public class AddressService {

    private final Configuration configuration;

    private GoogleService gServ;
    private TrafikLabService tls;

    private String gApiKey;

    @Inject
    public AddressService(Configuration configuration) {

        this.configuration = configuration;
        this.gApiKey=configuration.getString("googleAPIKey");

        /*this.gServ = new GoogleService(gApiKey);
        this.tls = new TrafikLabService(gApiKey);*/

    }

    public void gatherData(Address address) {

        /*address = GoogleService.getCoordinates(address,gApiKey);
        Logger.debug(address.streetName+", coords: "+address.latitude+", "+address.longitude);

        JsonNode jn=gServ.findNearestStation(address);
        Logger.debug(jn.toString());

        JsonNode jsonNodeTest = tls.getDistanceToCentralen(address);

        AddressDescription addressDescription = new AddressDescription();

        addressDescription.distances.add(new Distance("Centralen", null, jsonNodeTest.findValue("duration").asInt()));
        addressDescription.distances.add(new Distance("Tunnelbana", jsonNodeTest.findValue("distance").asInt(), null));

        addressDescription.save();

        address.addressDescription = addressDescription;*/

    }
}
