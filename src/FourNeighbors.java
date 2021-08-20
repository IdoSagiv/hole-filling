import org.opencv.core.Point;

import java.util.ArrayList;

public class FourNeighbors implements NeighborsFunction {
    @Override
    public ArrayList<Point> getAllNeighbors(Point p) {
        return new ArrayList<Point>() {{
            add(new Point(p.x + 1, p.y));
            add(new Point(p.x - 1, p.y));
            add(new Point(p.x, p.y + 1));
            add(new Point(p.x, p.y - 1));
        }};
    }
}
