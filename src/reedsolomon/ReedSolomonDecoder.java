package reedsolomon;

import java.util.Arrays;
import java.util.Hashtable;

import galoisfield.GFMatrix;
import galoisfield.GalloisField2;
import vandermonde.VandermondeMatrix;

public class ReedSolomonDecoder {

	private int numberOfParity;
	private int numberOfData;
	private Hashtable<Integer, Integer> knownData;
	private GFMatrix A;
	GalloisField2 GF2 = new GalloisField2(8); //8 for byte, 16 for int
	private int[] EPrime;
	private int[][] APrime;
	private int nbDataAndParity;

	public ReedSolomonDecoder(int numberOfData, int numberOfParity) {
		this.numberOfData = numberOfData;
		this.numberOfParity = numberOfParity;
		this.knownData = new Hashtable<Integer, Integer>();
		this.nbDataAndParity = this.numberOfData+this.numberOfParity;
		
		this.A = new GFMatrix(GF2, this.nbDataAndParity, this.numberOfData);
		this.A.setIdentity();
		
		VandermondeMatrix vanderMonde = new VandermondeMatrix(this.numberOfParity, this.numberOfParity, GF2);
		A.setMatrix(this.numberOfData, 0, vanderMonde);

		APrime = new int[this.numberOfData][this.numberOfData];
		EPrime = new int[this.numberOfData];
	}

	public void setData(int dataIndex, int data) {
		if(this.nbDataAndParity < dataIndex){
			throw new RuntimeException("index out of bound! "+dataIndex+" max="+this.nbDataAndParity);
		}
		this.knownData.put(dataIndex, data);
	}

	public int[] getDecoded() {
		if(this.knownData.size() < this.numberOfData){
			throw new RuntimeException("Not enough data to recover have "+this.knownData.size()+" need "+this.numberOfData);
		}		

		int[] dataIndexArray = new int[this.knownData.size()];
		int knownDataCounter=0;
		for(int dataIndex : this.knownData.keySet()){
			dataIndexArray[knownDataCounter] = dataIndex;
			knownDataCounter++;
		}
		Arrays.sort(dataIndexArray);
		knownDataCounter = dataIndexArray.length-1;
		for(int dataIndex : dataIndexArray){
			APrime[knownDataCounter] = A.getRow(dataIndex);
			EPrime[knownDataCounter] = this.knownData.get(dataIndex);
			knownDataCounter--;
		}
		
		GFMatrix GFAPrime = new GFMatrix(GF2, APrime);
		GFMatrix GFE = new GFMatrix(GF2, EPrime, EPrime.length);
//		GFAPrime.setIdentity();
		GFMatrix D = GFAPrime.solve(GFE);
		int[] recoveredByte = new int[this.numberOfData];
		for(int i=0; i<recoveredByte.length; i++){
			recoveredByte[i] = D.get(i, 0);
		}
		return recoveredByte;
	}

}
