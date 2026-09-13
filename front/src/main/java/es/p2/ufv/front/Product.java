package es.p2.ufv.front;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product
{
    private String name;
    private double price;

    @JsonProperty("market")
    private String supermarket;

    private String image;
    private String productPrice;
    private String priceUnitOrKg;
    private String brand;

    // NUEVO: campo quantity para manejo de cantidades
    private int quantity = 1;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSupermarket() { return supermarket; }
    public void setSupermarket(String supermarket) { this.supermarket = supermarket; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getProductPrice() { return productPrice; }
    public void setProductPrice(String productPrice) { this.productPrice = productPrice; }

    public String getPriceUnitOrKg() { return priceUnitOrKg; }
    public void setPriceUnitOrKg(String priceUnitOrKg) { this.priceUnitOrKg = priceUnitOrKg; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    private double originalPrice; // nuevo campo

    public double getOriginalPrice() {
        return originalPrice;
    }
    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }


}

