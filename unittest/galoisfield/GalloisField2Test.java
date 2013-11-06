package galoisfield;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class GalloisField2Test {

	@Test
	public void testGalloisField2() {
		GalloisField2 gf2_4 = new GalloisField2(4);

		assertEquals(0, gf2_4.sub(0, 0));
		assertEquals(0, gf2_4.multiply(0, 0));
		
		assertEquals(12, gf2_4.add(11, 7));
		assertEquals(12, gf2_4.sub(11, 7));
		
		assertEquals(9, gf2_4.multiply(3, 7));
		assertEquals(11, gf2_4.multiply(13, 10));

		assertEquals(3, gf2_4.divide(13, 10));
		assertEquals(10, gf2_4.divide(3, 7));
		
		assertEquals(5, gf2_4.pow(3, 2));
		assertEquals(4, gf2_4.pow(2, 2));
		assertEquals(1, gf2_4.pow(1, 2));

		assertEquals(1, gf2_4.pow(1, 1));
		assertEquals(1, gf2_4.pow(1, 0));
		assertEquals(1, gf2_4.pow(2, 0));
}

}
