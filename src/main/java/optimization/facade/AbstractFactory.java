package optimization.facade;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import optimization.painting.ImageFitCalculator;
import optimization.painting.ImageVectorGenetic;
import optimization.painting.utils.ImageSimilarity;

public class AbstractFactory {

	public BufferedImage loadSampleImage( String fileName ) {
		ImageIcon ico = new ImageIcon( fileName );
		
		int width = ico.getIconWidth();
		int height = ico.getIconHeight();
		
		BufferedImage ret = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
		Graphics2D g2 = (Graphics2D) ret.getGraphics();
		g2.drawImage( ico.getImage(), 0, 0, null );
		
		return ret;
	}
	
	public ImageFitCalculator getImageFitCalculator( BufferedImage sampleImage, ImageSimilarity imageSimilarity ) {
		ImageFitCalculator ifc = new ImageFitCalculator( sampleImage, imageSimilarity);
		return ifc;
	}
	
	public List<ImageVectorGenetic> createPopulation( int count, ImageFitCalculator fitCalculator,
													int polygonsCount, int vertexesCount,
													int maxWidth, int maxHeight,
													int rMaxRandVal, int gMaxRandVal, 
													int bMaxRandVal, int aMaxRandVal,
													double xMutRange, double yMutRange,
													double rMutRange, double gMutRange, 
													double bMutRange, double aMutRange) {
		List<ImageVectorGenetic> population = new LinkedList<ImageVectorGenetic>();
		
		for ( int i = 0; i < count; i++ )
		{
			ImageVectorGenetic ivg = new ImageVectorGenetic( polygonsCount, vertexesCount, fitCalculator);
			ivg.setXMutRange(xMutRange);
			ivg.setYMutRange(yMutRange);
			
			ivg.setRMutRange(rMutRange);
			ivg.setGMutRange(gMutRange);
			ivg.setBMutRange(bMutRange);
			ivg.setAMutRange(aMutRange);
			
			ivg.fillWithRandomRGBA( rMaxRandVal, gMaxRandVal, bMaxRandVal, aMaxRandVal);
			ivg.fillWithRandomCoords( maxWidth, maxHeight );
			population.add(ivg);
		}
		
		return population;
	}
	
	public List<ImageVectorGenetic> createPopulation( int count, ImageFitCalculator fitCalculator,
			int polygonsCount, int vertexesCount, BufferedImage sampleImage ) {
		
		int widthMutationInterval = sampleImage.getWidth() / 10;
		int heightMutationInterval = sampleImage.getHeight() / 10;
		
		widthMutationInterval = ( widthMutationInterval > 5 ) ? widthMutationInterval : 5;
		heightMutationInterval = ( heightMutationInterval > 5 ) ? heightMutationInterval : 5;
		
		return createPopulation( count, fitCalculator, polygonsCount, vertexesCount,
								sampleImage.getWidth(), sampleImage.getHeight(),
//								0, 0, 0, 0,
								255, 255, 255, 0,
								widthMutationInterval, heightMutationInterval, 
								255/20, 255/20, 255/20, 255/20 );
	}
	
}
