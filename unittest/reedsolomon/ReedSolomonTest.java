package reedsolomon;

import java.util.Random;

import junit.framework.TestCase;

public class ReedSolomonTest extends TestCase {
	final byte BYTE0 = 10;
	final byte BYTE1 = 11;
	final byte BYTE2 = 12;
	final byte BYTE3 = 13;
	final byte BYTE4 = 14;

	public void testReedSolomonSimple() {
		ReedSolomonParityGenerator rsEncoder = new ReedSolomonParityGenerator(5, 2);
		rsEncoder.setData(0, BYTE0);
		rsEncoder.setData(1, BYTE1);
		rsEncoder.setData(2, BYTE2);
		rsEncoder.setData(3, BYTE3);
		rsEncoder.setData(4, BYTE4);
		
		int[] parity = rsEncoder.getParity();
		assertNotNull(parity);
		assertEquals(2, parity.length);
//		assertEquals(74, parity[0]);
//		assertEquals(152, parity[1]);
//		assertEquals(302, parity[2]);

		
		ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(5, 2);
		rsDecoder.setData(0, BYTE0); //D0
		rsDecoder.setData(1, BYTE1); //D1
		rsDecoder.setData(3, BYTE3); //D3
		rsDecoder.setData(5, parity[0]); //C0
		rsDecoder.setData(6, parity[1]); //C1
		
		int[] decoded = rsDecoder.getDecoded();
		assertEquals(5, decoded.length);
		assertEquals(BYTE0, decoded[0]);
		assertEquals(BYTE1, decoded[1]);
		assertEquals(BYTE2, decoded[2]);
		assertEquals(BYTE3, decoded[3]);
		assertEquals(BYTE4, decoded[4]);
	}

	public void testReedSolomon() throws Exception {
		final int BLOCK_SIZE = 100;
		final int DATA_SIZE = 1000;
		int[] srcData = generateSrcData(DATA_SIZE);
		int nbBlocks = DATA_SIZE/BLOCK_SIZE;

		//We need BLOCK_SIZE generators since we parallely generate the partity data for all the blocks
		ReedSolomonParityGenerator rs[] = new ReedSolomonParityGenerator[BLOCK_SIZE];
		for(int i=0; i<BLOCK_SIZE; i++){
			rs[i] = new ReedSolomonParityGenerator(nbBlocks, 1);
		}

		for(int currentBlock=0; currentBlock<nbBlocks; currentBlock++){
			for(int blockOffset=0; blockOffset<BLOCK_SIZE; blockOffset+=4){
				int intData = srcData[currentBlock*BLOCK_SIZE+blockOffset+0] << 3*8 |
				srcData[currentBlock*BLOCK_SIZE+blockOffset+1] << 2*8 |
				srcData[currentBlock*BLOCK_SIZE+blockOffset+2] << 1*8 |
				srcData[currentBlock*BLOCK_SIZE+blockOffset+3] << 0*8;
				rs[blockOffset].setData(currentBlock, intData);
			}
		}
		int[] parityBlock = new int[BLOCK_SIZE];
		for(int i=0; i<BLOCK_SIZE; i++){
			parityBlock[i] = rs[i].getParity()[0];
		}
		assertEquals(BLOCK_SIZE, parityBlock.length);
	}

	private int[] generateSrcData(int data_size) {
		Random rnd = new Random();
		int[] srcData = new int[data_size];
		for(int i=0; i<srcData.length; i++){
			srcData[i] = (short) rnd.nextInt();
		}
		return srcData;
	}

}
