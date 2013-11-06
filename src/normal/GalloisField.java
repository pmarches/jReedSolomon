package normal;

public class GalloisField {
	public double add(double a, double b){
		return a+b;
	}

	public double sub(double a, double b){
		return a-b;
	}
	
	public double multiply(double a, double b){
		return a*b;
	}

	public double divide(double a, double b){
		return a/b;
	}

	public double pow(double base, double exponent) {
		return Math.pow(base, exponent);
	}

}
