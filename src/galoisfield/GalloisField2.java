package galoisfield;

import java.math.BigInteger;

/*
 * Math operations on GF(2^w)
 */
public class GalloisField2 {
	private static final int primitive_polynomial_4 = new BigInteger("10011", 2).intValue();
	private static final int primitive_polynomial_8 = new BigInteger("100011101", 2).intValue();
	private static final int primitive_polynomial_16 = new BigInteger("10001000000001011", 2).intValue();

	private int[] gflog;
	private int[] gfilog;
	private int fieldSize;
	private int fieldMask;
 
	public GalloisField2(int w){
		this.fieldSize = (int) Math.pow(2, w);
		this.fieldMask = ~(0xFFFFFFFF << w);
		this.gflog = new int[fieldSize];
		this.gfilog = new int[fieldSize];

		int primitivePolyNomial;
		switch(w){
			case 4:
				primitivePolyNomial = primitive_polynomial_4;
				break;
			case 8:
				primitivePolyNomial = primitive_polynomial_8;
				break;
			case 16:
				primitivePolyNomial = primitive_polynomial_16;
				break;
			default:
				throw new RuntimeException("Invalid value of w:"+w);
		}

		int b = 1;
		for(int log = 0; log < fieldSize-1; log++) {
			gflog[b] = log;
			gfilog[log] = b;
			b = b << 1;
			if((b & fieldSize) != 0){
				b = b ^ primitivePolyNomial;
			}
		}
	}
	
	public int add(int a, int b){
		return ((a & fieldMask) ^ (b & fieldMask)) & fieldMask;
	}

	public int sub(int a, int b){
		return ((a & fieldMask) ^ (b & fieldMask)) & fieldMask;
	}
	
	public int multiply(int a, int b){
		int sum_log;
		a &= fieldMask;
		b &= fieldMask;
		if (a == 0 || b == 0) return 0;
		sum_log = gflog[a] + gflog[b];
		if (sum_log >= this.fieldSize-1) sum_log -= this.fieldSize-1;
		return gfilog[sum_log];
	}

	public int divide(int a, int b){
		a &= fieldMask;
		b &= fieldMask;
		if (a == 0) return 0;
		if (b == 0){
			throw new RuntimeException("Can’t divide by 0");
		}
		int diff_log = gflog[a] - gflog[b];
		if (diff_log < 0) diff_log += this.fieldSize-1;
		return gfilog[diff_log];
	}

	public int pow(int base, int exponent) {
		if(exponent == 0) return 1;
		
		for(int i=1; i<exponent; i++){
			base = multiply(base, base);
		}
		return base;
	}

}
