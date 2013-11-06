package normal;

import junit.framework.TestCase;

public class MatrixTest extends TestCase {
	GalloisField gf = new GalloisField();
	private Matrix identity3;
	private Matrix mat3_3;
	private Matrix mat4_4;
	
	public void setUp(){
		mat3_3 = new Matrix(gf, new double[]{0,0,0, 1,1,1, 0,0,0}, 3);
		identity3 = new Matrix(gf, 3, 3);
		identity3.setAsIdentity();
		mat4_4 = new Matrix(gf, new double[]{0,0,0,0, 1,1,1,1, 0,0,0,0, 1,1,1,1}, 4);
		
		try{
			new Matrix(gf, new double[]{1,2,3,4}, 3);
			fail();
		}
		catch(RuntimeException e){} //OK!
	}
	

	public void testEqualsObject() {
		assertEquals(this.mat3_3, this.mat3_3);
		assertEquals(this.mat3_3, new Matrix(this.mat3_3));
		assertFalse(this.mat3_3.equals(this.mat4_4));
	}

	public void testSetIdentity(){
		for(int x=0; x<identity3.rowCount(); x++){
			assertEquals(1.0, identity3.get(x, x));
		}
	}

	public void testAdd() {
		try{
			mat3_3.add(mat4_4);
			fail();
		}
		catch(RuntimeException e){} //OK!
		
		Matrix result = mat3_3.add(identity3);
		Matrix expectedAddResult = new Matrix(gf, new double[]{1,0,0, 1,2,1, 0,0,1}, 3);
		assertEquals(expectedAddResult, result);
	}

	public void testSet() {
		try{
			this.identity3.set(4, 1, 0);
		}
		catch (RuntimeException e) {}

		assertEquals(1.0, this.identity3.get(1, 1));
		this.identity3.set(1, 1, 0);
		assertEquals(0.0, this.identity3.get(1, 1));

		this.identity3.set(1, 1, 16);
		assertEquals(16.0, this.identity3.get(1, 1));

		this.identity3.set(1, 1, 22);
		assertEquals(22.0, this.identity3.get(1, 1));
	}

	public void testSetMatrix() {
		//fail("Not yet implemented");
	}
	
	public void testAugment(){
		Matrix mat3Id = mat3_3.augment(this.identity3);
		assertEquals(mat3Id.rowCount(), 3);
		assertEquals(mat3Id.columnCount(), 6);
		assertEquals(this.mat3_3.get(0,0), mat3Id.get(0, 0));
		assertEquals(this.mat3_3.get(1,0), mat3Id.get(1, 0));
	}
	
	public void testGaussian(){
		Matrix A = new Matrix(this.gf, new double[]{1,0,0, 1,1,1, 1,2,3}, 3);
		Matrix E = new Matrix(this.gf, new double[]{3,11,9}, 3);
		//GFMatrix X = new GFMatrix(this.gf, GFGaussian.lsolve(gf, A.getArray(), E.getColumn(0)), 3);
	}
	
	public void testInverse(){
		double[][] inversable = new double[][]{
				{1,0,0},
				{1,1,1},
				{1,2,3}
		};
		double[][] inversed = new double[][]{
				{1, 0, 0},
				{-2, 3, -1},
				{1, -2, 1}
		};
		Matrix inversableMatrix = new Matrix(this.gf, inversable);
		assertEquals(new Matrix(this.gf, inversed), inversableMatrix.inverse());
		System.out.println(inversableMatrix.inverse());
	}

}
