package scenarioWeek;

/**
 * Created by shaniceong on 21/02/2017.
 */
public class Vector
{
    public double X;
    public double Y;
    double epsilon = 0.000000001;

    // Constructors.
    public Vector(double x, double y) { X = x; Y = y; }
    public Vector() {
        this(0, 0);
    }

    public Vector minus(Vector v, Vector w)
    {
        return new Vector(v.X - w.X, v.Y - w.Y);
    }

    public static Vector plus(Vector v, Vector w)
    {
        return new Vector(v.X + w.X, v.Y + w.Y);
    }

    public static double times(Vector v, Vector w)
    {
        return v.X * w.X + v.Y * w.Y;
    }

    public static Vector times(Vector v, double mult)
    {
        return new Vector(v.X * mult, v.Y * mult);
    }

    public static Vector times(double mult, Vector v)
    {
        return new Vector(v.X * mult, v.Y * mult);
    }

    public double Cross(Vector v)
    {
        return X * v.Y - Y * v.X;
    }

    public boolean Equals(Object obj)
{
    Vector v = (Vector)obj;
    return Math.abs((X - v.X)) < epsilon && Math.abs((Y - v.Y)) < epsilon;
}
}