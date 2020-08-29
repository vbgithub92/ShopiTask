package com.androiddev.shopitask.models;

import android.media.Image;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ShoppingItem implements Serializable {
    private UUID id;
    private String name;
    private double quantity;
    private UOM uom;
    private Image pic;

    public ShoppingItem(String name, double quantity, UOM uom, Image pic) {
        this.name = name;
        this.quantity = quantity;
        this.uom = uom;
        this.pic = pic;
        this.id = UUID.randomUUID();
    }

    public ShoppingItem(UUID id, String name, double quantity, UOM uom, Image pic) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.uom = uom;
        this.pic = pic;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(UOM uom) {
        this.uom = uom;
    }

    public Image getPic() {
        return pic;
    }

    public void setPic(Image pic) {
        this.pic = pic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingItem that = (ShoppingItem) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
