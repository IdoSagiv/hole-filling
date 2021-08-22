package HoleFiller;

import javafx.util.Pair;
import org.opencv.core.*;

import java.util.ArrayList;
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
        Pair<HashSet<Point>, HashSet<Pixel>> holeAndBoundary = findHoleAndBoundary(image, N);
        HashSet<Point> hole = holeAndBoundary.getKey();
        HashSet<Pixel> boundary = holeAndBoundary.getValue();
        Mat newImg = image.clone();

        for (Point u : hole) {
            double newVal = calcNewVal(u, boundary, W);
            newImg.put(u.x, u.y, newVal);
        }

        return newImg;
    }

    public static Mat fillHoleApprox(Mat image, WeightFunction W, NeighborsFunction N, int k) {
        Pair<HashSet<Point>, HashSet<Pixel>> holeAndBoundary = findHoleAndBoundary(image, N);
        HashSet<Point> hole = holeAndBoundary.getKey();
        HashSet<Pixel> boundary = pixelsReduction(holeAndBoundary.getValue(), k);
        Mat newImg = image.clone();

        for (Point u : hole) {
            double newVal = calcNewVal(u, boundary, W);
            newImg.put(u.x, u.y, newVal);
        }

        return newImg;
    }

    /**
     * reduces a given set of pixels to a smaller set of size k
     *
     * @param pixels original set of pixels
     * @param k      maximal number of new pixels
     * @return a set of min(k,|pixels|) pixels
     */
    private static HashSet<Pixel> pixelsReduction(HashSet<Pixel> pixels, int k) {
        HashSet<Pixel> newBoundary = new HashSet<>();
        ArrayList<Pixel> b = new ArrayList<>(pixels);
        b.sort((p1, p2) -> {
            if (p1.coordinate.x == p2.coordinate.x) {
                return Integer.compare(p1.coordinate.y, p2.coordinate.y);
            }
            return Integer.compare(p1.coordinate.x, p2.coordinate.x);
        });

        int currCount = 0;
        double currValSum = 0;
        ArrayList<Point> currPoints = new ArrayList<>();
        for (Pixel point : b) {
            if (currCount >= b.size() / k) {
                newBoundary.add(new Pixel(getCenter(currPoints), currValSum / currCount));
                currCount = 0;
                currValSum = 0;
                currPoints = new ArrayList<>();
            } else {
                currCount++;
                currValSum += point.value;
                currPoints.add(point.coordinate);
            }
        }

        if (currCount > 0) {
            newBoundary.add(new Pixel(getCenter(currPoints), currValSum / currCount));
        }

        return newBoundary;
    }

    private static Point getCenter(ArrayList<Point> points) {
        int xVals = 0;
        int yVals = 0;
        for (Point point : points) {
            xVals += point.x;
            yVals += point.y;
        }
        return new Point(xVals / points.size(), yVals / points.size());
    }

    /**
     * @param u - point to calculate new value for
     * @param B - set of all the boundary pixels in I
     * @return the new color value of I(u)
     */
    private static double calcNewVal(Point u, HashSet<Pixel> B, WeightFunction W) {
        double eqNumerator = 0;
        double eqDenominator = 0;
        for (Pixel v : B) {
            double currW = W.weight(u, v.coordinate);
            eqNumerator += (currW * v.value);
            eqDenominator += currW;
        }
        return eqNumerator / eqDenominator;
    }

    /**
     * @param I - grayscale image with float pixel values in the range [0, 1], and hole values which are marked with the value -1
     * @return a pair of two sets (hole_points, boundary_points)
     */
    private static Pair<HashSet<Point>, HashSet<Pixel>> findHoleAndBoundary(Mat I, NeighborsFunction N) {
        HashSet<Point> hole = new HashSet<>();
        HashSet<Pixel> boundary = new HashSet<>();
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
    private static HashSet<Pixel> getBoundaryNeighbors(Point u, Mat I, NeighborsFunction N) {
        HashSet<Pixel> boundaryPixels = new HashSet<>();
        for (Point n : N.getAllNeighbors(u)) {
            double val = I.get(n.x, n.y)[0];
            if (isPointInImage(n, I) && I.get(n.x, n.y)[0] != HOLE_VALUE) {
                boundaryPixels.add(new Pixel(n, val));
            }
        }
        return boundaryPixels;
    }

    private static boolean isPointInImage(Point p, Mat image) {
        return (p.x >= 0) && (p.x < image.height()) && (p.y >= 0) && (p.y < image.width());
    }
}