package HoleFiller;

import javafx.util.Pair;
import org.opencv.core.*;

import java.util.HashSet;

public class HoleFiller {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final double HOLE_VALUE = -1d;

    /***
     * get a grayscale image with a hole and return a new image where the hole is filled
     * @param image - grayscale image with float pixel values in the range [0, 1], and hole values which are marked with the value -1
     */
    public static Mat fillHole(Mat image, WeightFunction W, NeighborsFunction N) {
        Pair<HashSet<Point>, HashSet<Point>> holeAndBoundary = findHoleAndBoundary(image, N);
        HashSet<Point> hole = holeAndBoundary.getKey();
        HashSet<Point> boundary = holeAndBoundary.getValue();
        Mat newImg = image.clone();

        for (Point u : hole) {
            double newVal = calcNewVal(u, image, boundary, W);
            newImg.put(u.x, u.y, newVal);
        }

        return newImg;
    }

    /**
     * @param u - point to calculate new value for
     * @param I - grayscale image with float pixel values in the range [0, 1], and hole values which are marked with the value -1
     * @param B - set of all the boundary points in I
     * @return the new value of I(u)
     */
    private static double calcNewVal(Point u, Mat I, HashSet<Point> B, WeightFunction W) {
        double eqNumerator = 0;
        double eqDenominator = 0;
        for (Point v : B) {
            double currW = W.weight(u, v);
            eqNumerator += (currW * I.get(v.x, v.y)[0]);
            eqDenominator += currW;
        }
        return eqNumerator / eqDenominator;
    }

    /**
     * @param I - grayscale image with float pixel values in the range [0, 1], and hole values which are marked with the value -1
     * @return a pair of two sets (hole_points, boundary_points)
     */
    private static Pair<HashSet<Point>, HashSet<Point>> findHoleAndBoundary(Mat I, NeighborsFunction N) {
        HashSet<Point> hole = new HashSet<>();
        HashSet<Point> boundary = new HashSet<>();
        for (int i = 0; i < I.height(); i++) {
            for (int j = 0; j < I.width(); j++) {
                if (I.get(i, j)[0] == HOLE_VALUE) {
                    Point p = new Point(i, j);
                    hole.add(p);
                    boundary.addAll(getBoundaryNeighbors(p, I, N));
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
    private static HashSet<Point> getBoundaryNeighbors(Point u, Mat I, NeighborsFunction N) {
        HashSet<Point> boundary = new HashSet<>();
        for (Point n : N.getAllNeighbors(u)) {
            if (isPointInImage(n, I) && I.get(n.x, n.y)[0] != HOLE_VALUE) {
                boundary.add(n);
            }
        }
        return boundary;
    }

    private static boolean isPointInImage(Point p, Mat image) {
        return (p.x >= 0) && (p.x < image.height()) && (p.y >= 0) && (p.y < image.width());
    }
}