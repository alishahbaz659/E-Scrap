package com.jason.escrap.Model;

public class Products {
    String type, caption, producttype, manufacturingdate, expirydate,
            briefhistory, warranty, lendavailability, lendavailability_price,
            saleavailability, saleavailabilityprice, imageurl, uid;
    Double lattitude, longitude;

    public Products() {
    }

    public Products(String type, String caption, String producttype, String manufacturingdate, String expirydate,
                    String briefhistory, String warranty, String lendavailability, String lendavailability_price,
                    String saleavailability, String saleavailabilityprice, String imageurl, String uid
            , Double lattitude, Double longitude
    ) {
        this.type = type;
        this.caption = caption;
        this.producttype = producttype;
        this.manufacturingdate = manufacturingdate;
        this.expirydate = expirydate;
        this.briefhistory = briefhistory;
        this.warranty = warranty;
        this.lendavailability = lendavailability;
        this.lendavailability_price = lendavailability_price;
        this.saleavailability = saleavailability;
        this.saleavailabilityprice = saleavailabilityprice;
        this.imageurl = imageurl;
        this.uid = uid;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public String getCaption() {
        return caption;
    }

    public String getProducttype() {
        return producttype;
    }

    public String getManufacturingdate() {
        return manufacturingdate;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public String getBriefhistory() {
        return briefhistory;
    }

    public String getWarranty() {
        return warranty;
    }

    public String getLendavailability() {
        return lendavailability;
    }

    public String getLendavailability_price() {
        return lendavailability_price;
    }

    public String getSaleavailability() {
        return saleavailability;
    }

    public String getSaleavailabilityprice() {
        return saleavailabilityprice;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getUid() {
        return uid;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
