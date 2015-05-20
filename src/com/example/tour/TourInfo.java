package com.example.tour;

import java.io.Serializable;

public class TourInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String cityname;
	Double lng;
	Double lat;
	String abst;
	String dayname;
	String description;
	String path;
	public TourInfo(String cityname,Double lng,Double lat,String abst,String description,String path,String dayname){
		this.cityname = cityname;
		this.abst =abst;
		this.description = description;
		this.lat = lat;
		this.lng = lng;
		this.path = path;
	}
	public void setTourtime(String dayname){
		this.dayname = dayname;
	}
	public void setpath(String path){
		this.path = path;
	}
	public String getcityname(){
		return cityname;
	}
	public String getabst(){
		return abst;
	}
	public String getdescription(){
		return description;
	}
	public Double getlat(){
		return lat;
	}
	public Double getlng(){
		return lng;
	}
	public String getpath(){
		return path;
	}
	
}
