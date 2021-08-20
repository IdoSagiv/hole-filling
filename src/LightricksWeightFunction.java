import org.opencv.core.Point;

public class LightricksWeightFunction extends WeightFunction {
    LightricksWeightFunction(double z, double eps) {
        super(z, eps);
    }

    @Override
    double weight(Point u, Point v) {
        double subNorm = Math.sqrt(Math.pow(u.x - v.x, 2) + Math.pow(u.y - v.y, 2));

        return 1 / (Math.pow(subNorm, this.z) + this.eps);
    }
}
