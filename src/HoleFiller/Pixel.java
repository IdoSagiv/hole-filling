package HoleFiller;

import java.util.Objects;

public class Pixel {
    public Point coordinate;
    public double value;

    public Pixel(Point coordinate, double value) {
        this.coordinate = coordinate;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pixel)) return false;
        Pixel it = (Pixel) obj;
        return coordinate.equals(it.coordinate) && value == it.value;
    }

    @Override
    public int hashCode() {
        return String.format("%d,%d,%f", coordinate.x, coordinate.y, value).hashCode();
    }
}
