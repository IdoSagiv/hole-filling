package HoleFiller;

import java.util.ArrayList;

public class FourNeighbors implements NeighborsFunction {
    @Override
    public ArrayList<Point> getAllNeighbors(Point p) {
        int x = p.x;
        int y = p.y;
        return new ArrayList<Point>() {{
            add(new Point(x + 1, y));
            add(new Point(x - 1, y));
            add(new Point(x, y + 1));
            add(new Point(x, y - 1));
        }};
    }
}
