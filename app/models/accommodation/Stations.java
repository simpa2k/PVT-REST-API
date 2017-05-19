package models.accommodation;

import com.avaje.ebean.Model;

/**
 * Created by Tobias on 18/05/2017.
 */
public class Stations extends Model{
	
	public String stationName;
	public int distFromCentrum;
	
	public Stations(){
	
	}
	
	public void setStationName(String name){
		this.stationName=name;
	}
	public void setDistance(int dist){
		this.distFromCentrum=dist;
	}
}

