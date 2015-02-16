package jp.ac.anan_nct.smaoni_ver3.model;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by skriulle on 2015/02/16.
 */
public class Field {

    private ArrayList<Location> points;
    double maxLng, minLng, maxLat, minLat;

    private int fieldType;
    //0->Meratoric field, 1->strict appointed field

    public Field(){
        points = new ArrayList<Location>();
        fieldType = 0;
    }
    public Field(ArrayList<Location> points){
        this.points = points;
        maxLng = maxLat = Double.MIN_VALUE;
        minLng = minLat = Double.MAX_VALUE;
        fieldType = 0;
    }
    public Field(double maxLng, double minLng, double maxLat, double minLat){
        points = new ArrayList<Location>();
        this.maxLng = maxLng;
        this.minLng = minLng;
        this.maxLat = maxLat;
        this.minLat = minLat;
        fieldType = 0;
    }

    public void setField(ArrayList<Location> points){
        switch(fieldType){
            case 0:
                setField0(points);
                break;
            case 1:
                setField1(points);
                break;
        }
    }

    public ArrayList<Location> getPoints(){
        return points;
    }
    public void setPoints(ArrayList<Location> points){
        this.points = points;
    }

    public void addPoints(Location location){
        points.add(location);
    }

    private void setField0(ArrayList<Location> points){
        for(int i = 0; i < points.size(); i++){
            if(points.get(i).getLongitude() > maxLng) maxLng = points.get(i).getLongitude();
            else if(points.get(i).getLongitude() < minLng) minLng = points.get(i).getLongitude();
            if(points.get(i).getLatitude() > maxLat) maxLat = points.get(i).getLatitude();
            else if(points.get(i).getLatitude() < minLat) minLat = points.get(i).getLatitude();
        }
    }
    private void setField1(ArrayList<Location> points){
        //TODO
    }

    public void setFieldType(int fieldtype){
        this.fieldType = fieldtype;
    }
    public int getFieldType(){
        return fieldType;
    }
}
