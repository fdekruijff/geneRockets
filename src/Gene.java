import com.sun.javafx.geom.Vec2d;

public class Gene {

    private Vec2d vector;

    public Gene(Vec2d vector) {
        this.vector = vector;
    }

    public Vec2d getVector() {
        return vector;
    }

    public void setVector(Vec2d vector) {
        this.vector = vector;
    }
}
