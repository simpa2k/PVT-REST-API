package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Address;
import models.accommodation.AddressDescription;
import models.accommodation.Distance;
import models.stringDescriptors.AtmDescriptor;
import models.stringDescriptors.BankDescriptor;
import models.stringDescriptors.StringDescriptor;
import play.Configuration;
import play.Logger;
import repositories.AddressDescriptionRepository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Simon Olofsson
 */
public class AddressService {

    private final Configuration configuration;

    private GoogleService gServ;
    private TrafikLabService tls;

    private String gApiKey;
    private AddressDescriptionRepository addressDescriptionRepository;
    private Map<String, Function<JsonNode, StringDescriptor>> creators = new HashMap<>();

    @Inject
    public AddressService(Configuration configuration,
                          AddressDescriptionRepository addressDescriptionRepository) {

        this.configuration = configuration;
        this.gApiKey=configuration.getString("googleAPIKey");
        this.addressDescriptionRepository = addressDescriptionRepository;

        this.gServ = new GoogleService(gApiKey);
        this.tls = new TrafikLabService(gApiKey);

        setCreators();

    }

    private void setCreators() {

        creators.put("atm", AtmDescriptor::new);
        creators.put("bank", BankDescriptor::new);

        /*
         * Add more creators here.
         */

    }

    public Address gatherData(Address address) {

        address = GoogleService.getCoordinates(address,gApiKey);
        Logger.debug(address.streetName+", coords: "+address.latitude+", "+address.longitude);

        JsonNode jn=gServ.findNearestStation(address);
        Logger.debug(jn.toString());

        JsonNode jsonNodeTest = tls.getDistanceToCentralen(address);

        AddressDescription addressDescription = new AddressDescription();

        addressDescription.distances.add(new Distance("Centralen", null, jsonNodeTest.findValue("duration").asInt()));
        addressDescription.distances.add(new Distance("Tunnelbana", jsonNodeTest.findValue("distance").asInt(), null));

        ObjectNode results = gServ.gatherNearbyDataDescriptor(address);
        Logger.debug("Results of gathering nearby data:\n" + results.toString());

        Iterator<Map.Entry<String, JsonNode>> iter = results.fields();

        while (iter.hasNext()) {

            Map.Entry<String, JsonNode> next = iter.next();

            StringDescriptor descriptor = creators.get(next.getKey()).apply(next.getValue());
            addressDescription.stringDescriptors.add(descriptor.getDescription());

        }

        addressDescriptionRepository.save(addressDescription);
        address.addressDescription = addressDescription;

        return address;

    }
}
