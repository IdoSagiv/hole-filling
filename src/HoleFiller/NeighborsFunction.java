package HoleFiller;

import java.util.ArrayList;

public interface NeighborsFunction {
    /**
     * @param p - point to get neighbors os
     * @return coordinates of all the neighbors of p within the image
     */
    public  ArrayList<Point> getAllNeighbors(Point p);
}
