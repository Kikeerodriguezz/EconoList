package es.p2.ufv.econoList.Modelo;

import java.util.Objects;

public class Product implements Comparable<Product>
{
    private Market market;
    private String brand;
    private String name;
    private float price;
    private String priceUnitOrKg;
    private String image;

    // NUEVO: campo quantity para CRUD
    private int quantity = 1;  // Por defecto 1 unidad

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Market getMarket()
    {
        return market;
    }

    public void setMarket(Market market)
    {
        this.market = market;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public Float getPrice()
    {
        return price;
    }
    public void setPrice(float price)
    {
        this.price = price;
    }

    public String getPriceUnitOrKg()
    {
        return priceUnitOrKg;
    }
    public void setPriceUnitOrKg(String priceUnitOrKg)
    {
        this.priceUnitOrKg = priceUnitOrKg;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getProductPrice()
    {
        return String.format("%.2f €", this.price);
    }

    public String getSupermarket() {
        return market != null ? market.getName() : null;
    }

    @Override
    public int compareTo(Product p)
    {
        return Objects.compare(this.getPrice(), p.getPrice(), Float::compareTo);
    }
}