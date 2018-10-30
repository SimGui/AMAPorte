package ovh.webnlog.amaporte.model;

import java.util.List;

public class Amap {
    public String id;
    public String title;
    public Double latitude;
    public Double longitude;
    public Grower grower;
    public List<Grower> partnerGrowers;
    public List<Delivery> delivery;
    public Address address;
    public String description;
    public String contact;
    public String mail;
    public String website;
    public String facebookUrl;
}
