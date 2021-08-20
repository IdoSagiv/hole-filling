public class Vec2D {
    private double x;
    private double y;

    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vec2D other) {
        x += other.x;
        y += other.y;
    }

    public void sub(Vec2D other) {
        x -= other.x;
        y -= other.y;
    }

    public static Vec2D add(Vec2D first, Vec2D second) {
        return new Vec2D(first.x + second.x, first.y + second.y);
    }

    public static Vec2D sub(Vec2D first, Vec2D second) {
        return new Vec2D(first.x - second.x, first.y - second.y);
    }

    public static double norm(Vec2D vec) {
        return Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y, 2));
    }
}
