package com.example.pcmind.Model;

public class Product {
    private String Id;
    private String Name;
    private String Price;
    private String Img;
    public Product(){}
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }


    public Product(String id, String name, String price, String img) {
        Id = id;
        Name = name;
        Price = price;
        Img = img;
    }




}
