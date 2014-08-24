package optimization.painting.utils;

import static java.lang.Math.sqrt;

public class ImageSimilarityAdvanced extends ImageSimilarity {

	@Override
	public double distanceRGB(int r1, int g1, int b1, int r2, int g2, int b2) {		
		//according to http://www.compuphase.com/cmetric.htm
		
		int deltaR = r1 - r2;
		int deltaG = g1 - g2;
		int deltaB = b1 - b2;
	
		int rMean = ( r1 + r2 ) >> 1;
		
		return sqrt( ( ( (512 + rMean) * deltaR * deltaR ) >> 8 ) 
					+ 4 * deltaG * deltaG 
					+ ( ( (767-rMean) * deltaB * deltaB ) >> 8 ) );	
	}

}
