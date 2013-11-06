package reedsolomon;

import galoisfield.GalloisField2;
import vandermonde.VandermondeMatrix;

public class ReedSolomonParityGenerator {
	private GalloisField2 gf2;
	private VandermondeMatrix vandermondeEncodingMatrix;
	private int numberOfParity;
	private int[] parityByte;

	public ReedSolomonParityGenerator(int numberOfData, int numberOfParity){
		this.numberOfParity = numberOfParity;
		this.parityByte = new int[this.numberOfParity];
		
		this.gf2 = new GalloisField2(16);
		this.vandermondeEncodingMatrix = new VandermondeMatrix(numberOfData, numberOfParity, gf2);
	}
	
	public void setData(int dataIndex, int data){
		for(int i=0; i<this.numberOfParity; i++){
			int f = (int) this.vandermondeEncodingMatrix.get(i, dataIndex);
			int parity = this.gf2.multiply(f, data);
			this.parityByte[i] = this.gf2.add(this.parityByte[i], parity);
		}
	}

	public int[] getParity(){
		return this.parityByte;
	}
}
