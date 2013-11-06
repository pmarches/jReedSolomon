package galoisfield;

import java.util.Arrays;

public class GFMatrix {
	private GalloisField2 gf;
	int[][] data;
	private int rowCount;
	private int columnCount;

	public GFMatrix(GalloisField2 gf, int rowCount, int columnCount){
		this.gf = gf;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.data = new int[rowCount][columnCount];
	}
	
	public GFMatrix(GalloisField2 gf, int[] sourceData, int numberOfRows){
		this.gf = gf;
		this.rowCount = numberOfRows;
		this.columnCount = sourceData.length/rowCount;
		if(sourceData.length % rowCount != 0){
			throw new RuntimeException("Invalid row size :"+numberOfRows + " "+sourceData.length);
		}
		
		this.data = new int[this.rowCount][this.columnCount];
		for(int x=0; x<this.rowCount; x++){
			System.arraycopy(sourceData, x*this.columnCount, this.data[x], 0, this.columnCount);
		}
	}
	
	public GFMatrix(GalloisField2 gf, int[][] rawValues) {
		this.gf = gf;
		this.rowCount = rawValues.length;
		if(this.rowCount == 0){
			this.columnCount = 0;
		}
		else{
			this.columnCount = rawValues[0].length;
		}
		
		this.data = new int[this.rowCount][this.columnCount];
		for(int x=0; x<this.rowCount; x++){
			System.arraycopy(rawValues[x], 0, this.data[x], 0, this.columnCount);
		}
	}

	public GFMatrix(GFMatrix srcMat) {
		if(srcMat == null){
			throw new NullPointerException();
		}
		this.gf = srcMat.gf;
		this.rowCount = srcMat.rowCount;
		this.columnCount = srcMat.columnCount;
		this.data = new int[rowCount][columnCount];

		for(int x=0; x<srcMat.data.length; x++){
			System.arraycopy(srcMat.data[x], 0, this.data[x], 0, this.columnCount);
		}
	}

	public GFMatrix add(GFMatrix mat2){
		if(hasSameDimension(mat2)==false){
			throw new RuntimeException("Matrices are not the same size!");
		}
		GFMatrix result = new GFMatrix(this.gf, this.rowCount, this.columnCount);
		for(int x=0; x<this.rowCount; x++){
			for(int y=0; y<this.columnCount; y++){
				result.data[x][y] = gf.add(this.data[x][y], mat2.data[x][y]);
			}
		}
		return result;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		for(int x=0; x<this.rowCount; x++){
			for(int y=0; y<this.columnCount; y++){
				buffer.append(this.data[x][y]);
				buffer.append('\t');
			}
			buffer.append('\n');
		}
		return buffer.toString();
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof GFMatrix)) return false;
		GFMatrix mat2 = (GFMatrix) obj;
		if(hasSameDimension(mat2) == false) return false;
		for(int x=0; x<this.rowCount; x++){
			if(Arrays.equals(this.data[x], mat2.data[x]) == false) return false;
		}
		return true;
	}

	private boolean hasSameDimension(GFMatrix mat2) {
		if(mat2.columnCount != this.columnCount) return false;
		if(mat2.rowCount != this.rowCount) return false;
		
		return true;
	}

	public void set(int row, int column, int value) {
		checkBounds(row, column);
		this.data[row][column] = gf.add(0, value);
	}

	public int get(int row, int column){
		checkBounds(row, column);
		return this.data[row][column];
	}
	
	private void checkBounds(int row, int column) {
		if(row >= this.rowCount){
			throw new RuntimeException("Invalid row "+row);
		}
		if(column >= this.columnCount){
			throw new RuntimeException("Invalid column "+column);
		}
	}
	
	public void setMatrix(int startRow, int startColumn, GFMatrix subMatrix) {
		if(startRow+subMatrix.rowCount > this.rowCount){
			throw new RuntimeException("Sub-Matrix has too many row or is inserted too low");
		}
		if(startColumn+subMatrix.columnCount > this.columnCount){
			throw new RuntimeException("Sub-Matrix has too many columns or is inserted too much to the right");
		}
		
		for(int x=0; x<subMatrix.rowCount; x++){
			for(int y=0; y<subMatrix.columnCount; y++){
				this.set(startRow+x, startColumn+y, subMatrix.data[x][y]);
			}
		}
		
	}

	public void setAsIdentity() {
		if(this.rowCount != this.columnCount){
			throw new RuntimeException("Matrix is not square!");
		}
		clear();
		for(int x=0; x<this.rowCount; x++){
				this.data[x][x] = 1;
		}
	}

	private void clear() {
		for(int x=0; x<this.rowCount; x++){
			Arrays.fill(this.data[x], 0);
		}
	}

	public int rowCount() {
		return this.rowCount;
	}
	
	public int columnCount(){
		return this.columnCount;
	}
	
	public GFMatrix inverse () {
		GFMatrix identity = new GFMatrix(this.gf, this.rowCount, this.columnCount);
		identity.setAsIdentity();
		return solve(identity);
	}
	
   public GFMatrix solve(GFMatrix B) {
	   if(isSquareMatrix()){
		   return new GFLUDecomposition(this, this.gf).solve(B);
	   }
	   else{
		   throw new RuntimeException("Cant solve non-square matrices yet..");
		   //new QRDecomposition(this)).solve(B);
	   }
	}

	private boolean isSquareMatrix() {
		return this.rowCount == this.columnCount;
	}
	
	public int[][] getArrayCopy() {
		int[][] copy = new int[this.rowCount][this.columnCount];
		for(int x=0; x<this.rowCount; x++){
			for(int y=0; y<this.columnCount; y++){
				copy[x][y] = this.data[x][y];
			}
		}
		return copy;
	}

	public int[][] getArray() {
		return this.data;
	}

	public GFMatrix getSubMatrix(int[] rowsWanted, int startColumn, int endColumn) {
		GFMatrix newMatrix = new GFMatrix(this.gf, rowsWanted.length, endColumn-startColumn+1);
		for(int x=0; x<rowsWanted.length; x++){
			for(int y=startColumn; y<=endColumn; y++){
				newMatrix.data[x][y]=this.data[rowsWanted[x]][y];
			}
		}
		return newMatrix;
	}


	public void swapRows(int row1, int row2){
		if(row1 >= this.rowCount){
			throw new RuntimeException("Invalid row1 to swap " + row1);
		}
		if(row2 >= this.rowCount){
			throw new RuntimeException("Invalid row2 to swap " + row2);
		}
		
		int[] tmp = this.data[row1];
		this.data[row1] = this.data[row2];
		this.data[row2] = tmp;
	}
	
	public void rowMultiply(int rowIndex, int multiplier){
		if(rowIndex >= this.rowCount){
			throw new RuntimeException("Invalid rowIndex specified" + rowIndex);
		}
		for(int x=0; x<this.columnCount; x++){
			this.data[rowIndex][x] = this.gf.multiply(this.data[rowIndex][x], multiplier);
		}
	}

	public void rowAdd(int rowIndex, int adder){
		if(rowIndex >= this.rowCount){
			throw new RuntimeException("Invalid rowIndex specified" + rowIndex);
		}
		for(int x=0; x<this.columnCount; x++){
			this.data[rowIndex][x] = this.gf.add(this.data[rowIndex][x], adder);
		}
	}
	
	public void insertMatrix(int rowPosition, int columnPosition, GFMatrix matrixToInsert){
		
		int[][] newData = new int[this.rowCount+matrixToInsert.rowCount][this.columnCount+matrixToInsert.columnCount];
		this.rowCount+=matrixToInsert.rowCount;
		this.columnCount+=matrixToInsert.columnCount;
		for(int y=0; y<this.rowCount; y++){
			for(int x=0; x<this.columnCount; x++){
				if(x<columnPosition){
					newData[x][y] = this.data[x][y];
				}
				else if(x>=rowPosition){
					newData[x][y] = matrixToInsert.data[x-matrixToInsert.rowCount][y];
				}
			}
		}
		this.data = newData;
	}

	public int[] getColumn(int columnIndex) {
		int[] column = new int[this.rowCount];
		for(int i=0; i<columnCount; i++){
			column[i] = this.data[i][columnIndex];
		}
		return column;
	}

	public GFMatrix augment(GFMatrix augmentation) {
		GFMatrix result = new GFMatrix(this.gf, this.rowCount, this.columnCount+augmentation.rowCount);
		for(int y=0; y<this.rowCount; y++){
			for(int x=0; x<this.columnCount; x++){
				result.data[y][x] = this.data[y][x];
			}
		}
		
		//Now copy the augmented part
		for(int y=0; y<augmentation.rowCount; y++){
			for(int x=0; x<augmentation.columnCount; x++){
				result.data[y][x+this.columnCount] = augmentation.data[y][x];
			}
		}
		
		return result;
	}

	public void deleteRow(int rowIndexToDelete) {
		int[][] newData = new int[this.rowCount-1][this.columnCount];
		int goodRowCounter = 0;
		for(int i=0; i<this.rowCount; i++){
			if(i!=rowIndexToDelete){
				newData[goodRowCounter] = this.data[i];
				goodRowCounter++;
			}
		}
		this.rowCount--;
		this.data = newData;
	}

	public int[] getRow(int dataIndex) {
		return this.data[dataIndex];
	}

	public void setIdentity() {
		int smallestdimension = Math.min(this.columnCount, this.rowCount);
		for(int i=0; i<smallestdimension; i++){
			this.set(i, i, 1); //Identity part
		}
	}
}
