package optimization.painting.utils;


public class ImageSimilaritySqrSum extends ImageSimilarity {
	
	@Override
	public double distanceRGB ( int r1, int g1, int b1, int r2, int g2, int b2 ) {
				
		int deltaR = r1 - r2;
		int deltaG = g1 - g2;
		int deltaB = b1 - b2;
		
		return 	( deltaR * deltaR 
				+ deltaG * deltaG 
				+ deltaB * deltaB );
	}

}
