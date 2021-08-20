import javafx.util.Pair;
import org.opencv.core.*;

import java.util.HashSet;

public class HoleFiller {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final double HOLE_VALUE = -1d;

    private final WeightFunction W;
    private final NeighborsFunction N;

    public HoleFiller(WeightFunction W, NeighborsFunction N) {
        this.W = W;
        this.N = N;
    }

    /***
     * get a grayscale image with a hole and return a new image where the hole is filled
     * @param image - grayscale image with float pixel values in the range [0, 1], and hole values which are marked with the value -1
     */
    public Mat fillHole(Mat image) {
        Pair<HashSet<Point>, HashSet<Point>> holeAndBoundary = findHoleAndBoundary(image);
        HashSet<Point> hole = holeAndBoundary.getKey();
        HashSet<Point> boundary = holeAndBoundary.getValue();
        Mat newImg = image.clone();

        for (Point u : hole) {
            double newVal = calcNewVal(u, image, boundary);
            newImg.put((int) u.x, (int) u.y, newVal);
        }

        return newImg;
    }

    /**
     * @param u - point to calculate new value for
     * @param I - grayscale image with float pixel values in the range [0, 1], and hole values which are marked with the value -1
     * @param B - set of all the boundary points in I
     * @return the new value of I(u)
     */
    private double calcNewVal(Point u, Mat I, HashSet<Point> B) {
        double eqNumerator = 0;
        double eqDenominator = 0;
        for (Point v : B) {
            double currW = W.weight(u, v);
            eqNumerator += (currW * I.get((int) v.x, (int) v.y)[0]);
            eqDenominator += currW;
        }
        return eqNumerator / eqDenominator;
    }

    /**
     * @param I - grayscale image with float pixel values in the range [0, 1], and hole values which are marked with the value -1
     * @return a pair of two sets (hole_points, boundary_points)
     */
    private Pair<HashSet<Point>, HashSet<Point>> findHoleAndBoundary(Mat I) {
        HashSet<Point> hole = new HashSet<>();
        HashSet<Point> boundary = new HashSet<>();
        for (int i = 0; i < I.height(); i++) {
            for (int j = 0; j < I.width(); j++) {
                if (I.get(i, j)[0] == HOLE_VALUE) {
                    Point p = new Point(i, j);
                    hole.add(p);
                    boundary.addAll(getBoundaryNeighbors(p, I));
                }
            }
        }
        return new Pair<>(hole, boundary);
    }

    /**
     * @param u - hole point (I(u)=-1)
     * @param I - grayscale image with float pixel values in the range [0, 1], and hole values which are marked with the value -1
     * @return a set with all the boundary points that are u neighbors
     */
    private HashSet<Point> getBoundaryNeighbors(Point u, Mat I) {
        HashSet<Point> boundary = new HashSet<>();
        for (Point n : N.getAllNeighbors(u)) {
            if (I.get((int) n.x, (int) n.y)[0] != HOLE_VALUE) {
                boundary.add(new Point((int) n.x, (int) n.y));
            }
        }
        return boundary;
    }
}
