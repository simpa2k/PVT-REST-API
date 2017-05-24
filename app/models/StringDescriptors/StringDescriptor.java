package models.StringDescriptors;

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

public class StringDescriptor {

    public String annonsText;
    private Configuration config;
    public Address address;
    public final String [] descriptorTypes =  {
            "atm", "bank", "casino", "cemetery", "church", "fire_station",
            "funeral_home", "hardware_store", "hindu_temple", "jewelery_store",
            "locksmith", "mosque", "painter", "pet_store", "rv_park", "synagogue" };
    ArrayList<String> allDescriptionStrings = new ArrayList<>();


@Inject
    public StringDescriptor(Configuration config, Address address) {
    this.config = config;
    this.address = address;

    }

    public void createAnnonsText() {
        GoogleService gserv = new GoogleService (config.getString("googleAPIKey"));

        ObjectNode node = gserv.gatherNearbyDataDescriptor(address);
        Logger.debug(node.toString());

    saveAddTextToClass(node);
    //    return node.toString();


    }


    public ArrayList<String> saveAddTextToClass(ObjectNode o){

        for (String types : descriptorTypes){
        if((o.get(types)!=null)){
            switch(types){
                case "mosque":
                    MosqueDescriptor mosqueDescriptor = new MosqueDescriptor(o);
                    allDescriptionStrings.add(mosqueDescriptor.toString());
                break;

                case "atm":
                    AtmDescriptor atmDescriptor = new AtmDescriptor(o);
                    allDescriptionStrings.add(atmDescriptor.toString());
                break;
                case "bank":
                    BankDescriptor bankDescriptor = new BankDescriptor(o);
                    allDescriptionStrings.add(bankDescriptor.toString());
                break;

            }
         }
    }

    return allDescriptionStrings;



    }




}

