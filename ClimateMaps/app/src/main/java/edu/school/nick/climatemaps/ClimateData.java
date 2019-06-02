package edu.school.nick.climatemaps;

/**
 * Created by Homepc on 6/10/2017.
 */

public class ClimateData {

    private String region;
    private String parameter;
    private String unit;
    private int range;
    private double value;
    private String subParameter;
    private boolean hasSubType;

    public Boolean getHasSubType() {
        return hasSubType;
    }

    public void setHasSubType(Boolean hasSubType) {
        this.hasSubType = hasSubType;
    }


    /************ Contructor ******************/
    public ClimateData (String region, String parameter, String unit, int range, double value)  {
        this.region = region;
        this.parameter = parameter;
        this.unit = unit;
        this.range = range;
        this.value = value;
    }
    /******************************************/

    /************ Getter methods **************/
    public String GET_region () {
        return region;
    }

    public String GET_parameter () {
        return parameter;
    }

    public String GET_unit () {
        return unit;
    }

    public int GET_range () {
        return range;
    }

    public double GET_value () {
        return value;
    }

    public String GET_subParameter () { return subParameter; }

    public String GET_SubParameter() {
        return subParameter;
    }
    /*******************************************/

    /************ Setter methods **************/
    public void SET_region (String r) {
        r = region;
    }

    public void SET_parameter (String p) {
        p = parameter;
    }

    public void SET_unit (String u) {
        u = unit;
    }

    public void SET_range (int r) {
        r = range;
    }

    public void SET_value (double v) {
        v = value;
    }

    public void SET_SubParameter(String subParameter) {
        this.subParameter = subParameter;
    }
    /*******************************************/

};
