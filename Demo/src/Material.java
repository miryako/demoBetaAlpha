public class Material {
    String id, name;
    int percentageOfDefects;

    public Material(String id, String name, int percentageOfDefects) {
        this.id = id;
        this.name = name;
        this.percentageOfDefects = percentageOfDefects;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPercentageOfDefects() {
        return percentageOfDefects;
    }

    public void setPercentageOfDefects(int percentageOfDefects) {
        this.percentageOfDefects = percentageOfDefects;
    }
}
