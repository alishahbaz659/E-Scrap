package com.jason.escrap.Model;

public class Users {
    String name, email, address, phoneno, description, uid, productowner, productseller, general;
    Double lattitude, longitude;


    public Users(String name, String email, String address, String phoneno, String description, String uid,
                 String productowner, String productseller, String general, Double lattitude, Double longitude) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneno = phoneno;
        this.description = description;
        this.uid = uid;
        this.productowner = productowner;
        this.productseller = productseller;
        this.general = general;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public Users() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getDescription() {
        return description;
    }

    public String getUid() {
        return uid;
    }

    public String getProductowner() {
        return productowner;
    }

    public String getProductseller() {
        return productseller;
    }

    public String getGeneral() {
        return general;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
