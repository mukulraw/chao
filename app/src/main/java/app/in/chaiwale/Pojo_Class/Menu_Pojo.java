package app.in.chaiwale.Pojo_Class;

public class Menu_Pojo {
    String id,menuname,price,subenu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubenu() {
        return subenu;
    }

    public void setSubenu(String subenu) {
        this.subenu = subenu;
    }

    public Menu_Pojo(String id, String menuname, String price, String subenu) {
        this.id = id;
        this.menuname = menuname;
        this.price = price;

        this.subenu = subenu;
    }
}
