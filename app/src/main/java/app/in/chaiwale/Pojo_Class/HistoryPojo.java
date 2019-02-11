package app.in.chaiwale.Pojo_Class;

public class HistoryPojo {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getDatee() {
        return datee;
    }

    public void setDatee(String datee) {
        this.datee = datee;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWalletdud() {
        return walletdud;
    }

    public void setWalletdud(String walletdud) {
        this.walletdud = walletdud;
    }

    public String getCodamount() {
        return codamount;
    }

    public void setCodamount(String codamount) {
        this.codamount = codamount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HistoryPojo(String id, String orderid, String datee, String price, String status, String walletdud, String codamount , String method) {
        this.id = id;
        this.orderid = orderid;
        this.datee = datee;
        this.price = price;
        this.status = status;
        this.walletdud = walletdud;
        this.codamount = codamount;
        this.method = method;

    }

    String id,orderid,datee,price,status,walletdud,codamount , method;


}
