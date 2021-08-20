import org.opencv.core.Point;

import java.util.ArrayList;

public class EightNeighbors implements NeighborsFunction {
    @Override
    public ArrayList<Point> getAllNeighbors(Point p) {
        ArrayList<Point> neighbors = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == j && i == 0) {
                    continue;
                }
                neighbors.add(new Point(p.x + i, p.y + j));
            }
        }
        return neighbors;
    }
}
