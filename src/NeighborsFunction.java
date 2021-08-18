import java.awt.*;
import java.util.ArrayList;

public abstract class NeighborsFunction {

    protected final int width;
    protected final int height;

    public NeighborsFunction(int imgWidth, int imgHeight) {
        this.width = imgWidth;
        this.height = imgHeight;
    }

    /**
     * @param p - point to get neighbors os
     * @return coordinates of all the neighbors of p within the image
     */
    public abstract ArrayList<org.opencv.core.Point> getAllNeighbors(org.opencv.core.Point p);
}
