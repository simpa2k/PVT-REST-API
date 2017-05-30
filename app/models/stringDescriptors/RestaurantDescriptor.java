package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;

import javax.persistence.*;

/**
 * Created by SimonSchwieler on 2017-05-30.
 */
@Entity
@Inheritance
@DiscriminatorValue("REST")
public class RestaurantDescriptor extends StringDescriptor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public RestaurantDescriptor(JsonNode node){




        super(node, new String[] {"Lägenheten ligger i ett mycket restaurangtätt område.",
                                    "Lägenheten ligger i ett restaurangtätt område.",
                                    "Det finns restauranger i lägenhetens närområde."});



        //atmDescription = String.format(chooseRandomDescriptionString(), atmName);

    }




    public String toString(){
        return description;
    }

    @Override
    public String generateDescription() {


        if(node.size() >= 18){
            description = possibleDescriptions[0];
        }
        else if(node.size() >= 10){
            description = possibleDescriptions[1];
        }
        else if(node.size() >= 5){
            description = possibleDescriptions[2];
        }else{
            description = null;
        }
        return description;
    }





}
