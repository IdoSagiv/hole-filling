package HoleFiller;

public abstract class WeightFunction {
    protected final double z;
    protected final double eps;

    public WeightFunction(double z, double eps) {
        this.z = z;
        this.eps = eps;
    }

    abstract double weight(Point u, Point v);
}
