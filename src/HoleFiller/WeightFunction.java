package HoleFiller;

@FunctionalInterface
public interface WeightFunction {
    double weight(Point u, Point v);
}
