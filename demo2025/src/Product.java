public class Product {
    String name, date;
    int count;

    public Product(String name, String date, int count) {
        this.name = name;
        this.date = date;
        this.count = count;
    }

    @Override
    public String toString() {
        return count + " штук  " + name + " за " + date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
