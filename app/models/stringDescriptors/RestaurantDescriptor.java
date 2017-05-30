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
            description = "Lägenheten ligger i ett mycket restaurangtätt område.";
        }
        else if(node.size() >= 10){
            description = "Lägenheten ligger i ett restaurangtätt område.";
        }
        else if(node.size() >= 1){
            description = "Det finns restauranger i lägenhetens närområde.";
        }else{
            description = "Det finns inga restauranger i närområdet";
        }
        return description;
    }





}
