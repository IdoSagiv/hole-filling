package HoleFiller;

import java.util.ArrayList;

public class EightNeighbors implements NeighborsFunction {
    @Override
    public ArrayList<Point> getAllNeighbors(Point p) {
        int x = p.x;
        int y = p.y;
        ArrayList<Point> neighbors = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == j && i == 0) {
                    continue;
                }
                neighbors.add(new Point(x + i, y + j));
            }
        }
        return neighbors;
    }
}
