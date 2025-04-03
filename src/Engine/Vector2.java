package Engine;

/**
 * Creates a Vector 2D class
 */
public class Vector2 {
	
	public double X = 0.0;
	public double Y = 0.0;
	
	
	public Vector2(double x, double y)
	{
		X = x;
		Y = y;
	}
	
	public void set(double x, double y)
	{
		X = x;
		Y = y;
	}
	
	public double magnitude() {
		return (double)Math.sqrt(X*X+Y*Y);
	}
	
	public void add(Vector2 v)
	{
		X += v.X;
		Y += v.Y;
	}
	
	public void add(double x, double y) {
		X += x;
		Y += y;
	}
	
	public void multiply(double n) {
		X *= n;
		Y *= n;
	}
	
	public void div(double n)
	{
		X /=n;
		Y /= n;
	}
	
	public void normalize()
	{
		double m = magnitude();
		if(m != 0 && m != 1) {
			div(m);
		}
	}
	
	public void limit(double max) {
		if(magnitude() > max) {
			normalize();
			multiply(max);
		}
	}
	
	static public Vector2 subtract(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.X-v2.X, v1.Y - v2.Y);
	}
	
	public double heading2() {
		return Math.atan2(Y, X);
	}
}
