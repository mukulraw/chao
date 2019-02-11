package app.in.chaiwale.Pojo_Class;

public class getCartPojo {
    String userId;
    String cart_id;
    String category_id;
    String category_name;
    String product_id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getToprice() {
        return toprice;
    }

    public void setToprice(String toprice) {
        this.toprice = toprice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public getCartPojo(String userId, String cart_id, String category_id, String category_name, String product_id, String product_name, String quantity, String unitprice, String toprice, String image) {
        this.userId = userId;

        this.cart_id = cart_id;
        this.category_id = category_id;
        this.category_name = category_name;
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.unitprice = unitprice;
        this.toprice = toprice;
        this.image = image;
    }

    String product_name;
    String quantity;
    String unitprice;
    String toprice;
    String image;
}
