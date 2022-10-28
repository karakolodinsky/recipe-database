package login;

public class Ingredient {
    public String name;
    private Integer quantity;
    private String unit;

    public Ingredient(String name, Integer quantity, String unit){
        this.name = name; this.quantity=quantity; this.unit=unit;
    }

    public String getName(){ return this.name; }

    public Integer getQuantity(){ return this.quantity; }

    public String getUnit(){ return this.unit; }
}
