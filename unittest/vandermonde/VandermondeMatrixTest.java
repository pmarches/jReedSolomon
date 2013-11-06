package vandermonde;

import galoisfield.GFMatrix;
import galoisfield.GalloisField2;
import junit.framework.TestCase;

public class VandermondeMatrixTest extends TestCase {

	public void testVandermondeMatrix() {
		int[][] threeByThreeValues =
			{
				{1, 1, 1}, 
				{1, 2, 3},
				{1, 4, 5}
			};
		GalloisField2 gf = new GalloisField2(4);
		GFMatrix expectedMatrix = new GFMatrix(gf, threeByThreeValues);
		
		VandermondeMatrix vm = new VandermondeMatrix(3, 3, gf);
		assertTrue(vm.equals(expectedMatrix));
	}

}
