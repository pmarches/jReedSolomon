package galoisfield;

import junit.framework.TestCase;

public class GFMatrixTest extends TestCase {
	GalloisField2 gf = new GalloisField2(4);
	private GFMatrix identity3;
	private GFMatrix mat3_3;
	private GFMatrix mat4_4;
	
	public void setUp(){
		mat3_3 = new GFMatrix(gf, new int[]{0,0,0, 1,1,1, 0,0,0}, 3);
		identity3 = new GFMatrix(gf, 3, 3);
		identity3.setAsIdentity();
		mat4_4 = new GFMatrix(gf, new int[]{0,0,0,0, 1,1,1,1, 0,0,0,0, 1,1,1,1}, 4);
		
		try{
			new GFMatrix(gf, new int[]{1,2,3,4}, 3);
			fail();
		}
		catch(RuntimeException e){} //OK!
	}
	

	public void testEqualsObject() {
		assertEquals(this.mat3_3, this.mat3_3);
		assertEquals(this.mat3_3, new GFMatrix(this.mat3_3));
		assertFalse(this.mat3_3.equals(this.mat4_4));
	}

	public void testSetIdentity(){
		for(int x=0; x<identity3.rowCount(); x++){
			assertEquals(1, identity3.get(x, x));
		}
	}

	public void testAdd() {
		try{
			mat3_3.add(mat4_4);
			fail();
		}
		catch(RuntimeException e){} //OK!
		
		GFMatrix result = mat3_3.add(identity3);
		GFMatrix expectedAddResult = new GFMatrix(gf, new int[]{1,0,0, 1,0,1, 0,0,1}, 3);
		assertEquals(expectedAddResult, result);
	}

	public void testSet() {
		try{
			this.identity3.set(4, 1, 0);
		}
		catch (RuntimeException e) {}

		assertEquals(1, this.identity3.get(1, 1));
		this.identity3.set(1, 1, 0);
		assertEquals(0, this.identity3.get(1, 1));

		this.identity3.set(1, 1, 16);
		assertEquals(0, this.identity3.get(1, 1));

		this.identity3.set(1, 1, 22);
		assertEquals(6, this.identity3.get(1, 1));
	}

	public void testSetMatrix() {
		//fail("Not yet implemented");
	}
	
	public void testAugment(){
		GFMatrix mat3Id = mat3_3.augment(this.identity3);
		assertEquals(mat3Id.rowCount(), 3);
		assertEquals(mat3Id.columnCount(), 6);
		assertEquals(this.mat3_3.get(0,0), mat3Id.get(0, 0));
		assertEquals(this.mat3_3.get(1,0), mat3Id.get(1, 0));
	}
	
	public void testGaussian(){
		GFMatrix A = new GFMatrix(this.gf, new int[]{1,0,0, 1,1,1, 1,2,3}, 3);
		GFMatrix E = new GFMatrix(this.gf, new int[]{3,11,9}, 3);
		//GFMatrix X = new GFMatrix(this.gf, GFGaussian.lsolve(gf, A.getArray(), E.getColumn(0)), 3);
	}
	
	public void testInverse(){
		int[][] inversable = new int[][]{
				{1,0,0},
				{1,1,1},
				{1,2,3}
		};
		int[][] inversed = new int[][]{
				{1,0,0},
				{2,3,1},
				{3,2,1}
		};
		GFMatrix inversableMatrix = new GFMatrix(this.gf, inversable);
		assertEquals(new GFMatrix(this.gf, inversed), inversableMatrix.inverse());
	}
	
	public void testDeleteRow(){
		assertEquals(1, identity3.get(0, 0));
		this.identity3.deleteRow(0);
		assertEquals(2, identity3.rowCount());
		assertEquals(0, identity3.get(0, 0));
		assertEquals(1, identity3.get(0, 1));
		assertEquals(0, identity3.get(0, 2));
	}

}
