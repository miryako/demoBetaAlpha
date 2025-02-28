public class PartnerProduct {
    String product, partner, quantity, date;

    public PartnerProduct(String product, String partner, String quantity, String date) {
        this.product = product;
        this.partner = partner;
        this.quantity = quantity;
        this.date = date;
    }

    @Override
    public String toString() {
        return partner + ": " + product + " (" + quantity + "шт) " + date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
