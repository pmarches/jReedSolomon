import reedsolomon.ReedSolomonTest;
import vandermonde.VandermondeMatrixTest;
import galoisfield.GFMatrixTest;
import galoisfield.GalloisField2Test;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for default package");
		//$JUnit-BEGIN$
		suite.addTestSuite(GFMatrixTest.class);
		suite.addTestSuite(GalloisField2Test.class);
		suite.addTestSuite(ReedSolomonTest.class);
		suite.addTestSuite(VandermondeMatrixTest.class);
		//$JUnit-END$
		return suite;
	}

}
