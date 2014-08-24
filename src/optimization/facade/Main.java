package optimization.facade;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import optimization.GeneticAlgorithm;
import optimization.painting.ImageFitCalculator;
import optimization.painting.ImageVectorGenetic;
import optimization.painting.ImgFitCalc2;
import optimization.painting.utils.ImageSimilaritySqrMulCos;
//import optimization.painting.utils.ImageSimilaritySqrSum;

public class Main {
	
	private static volatile boolean nextSchedule = false;
	private static int currPictureNum = 1;

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		final AbstractFactory af = new AbstractFactory();
		
		final BufferedImage sampleImage = af.loadSampleImage("images/apple.jpg");
		
		final boolean dump = false;
		final int dumpInterval = 3;
		
		final double scaleCoeff = 1.1;
		final double widthScale = 1.0;
		
		final int scaledImgWidth = (int)( sampleImage.getWidth() * scaleCoeff * widthScale );
		final int scaledImgHeight = (int)( sampleImage.getHeight() * scaleCoeff );		
		
		JFrame mainFrame = new JFrame("Test");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize( scaledImgWidth + 70, scaledImgHeight + 50 );
		
		final BufferedImage bi = new BufferedImage( scaledImgWidth, scaledImgHeight, BufferedImage.TYPE_INT_RGB);
		final Graphics2D big = bi.createGraphics();		
		//Antialiasing
		big.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		final JPanel panel = new JPanel()		
		{
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.drawImage(bi, 0, 0, null);
			}
		};		
		
		panel.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("==========================");
				nextSchedule = true;
			}
		});
		
		mainFrame.getContentPane().add(panel);
		
		mainFrame.setVisible(true);
		
		Thread t = new Thread( new Runnable() {
			@Override
			public void run() {				
				
				final List<ImageVectorGenetic> bestVectors = new LinkedList<ImageVectorGenetic>();
				
				for( int i = 0; i < 4; i++)
				{
					nextSchedule = false;
					
					ImageFitCalculator fitCalc = new ImgFitCalc2( sampleImage, new ImageSimilaritySqrMulCos(), bestVectors );
					
//					List<ImageVectorGenetic> population = af.createPopulation( 3, fitCalc, 50, 8, sampleImage);
					List<ImageVectorGenetic> population = af.createPopulation( 3, fitCalc, (i+1)*10, 8, sampleImage);
					
					final GeneticAlgorithm<ImageVectorGenetic> ga_2 = new GeneticAlgorithm<ImageVectorGenetic>( population );
					
					ga_2.setElitarParentsCount(1);
					ga_2.setParentsCount(2);
					ga_2.setPCrossover(0.4);
					ga_2.setPMutation(0.3);
					
					final Observer obs = new Observer(){
						private long fit = 0;
						private int i = 0;
						
						private int unChangedIterations = 0; 
						@Override
						public void update(Observable o, Object arg) {
							long currFit = ( (Double)arg ).longValue();
							if ( fit != currFit )
							{
								fit = currFit;
								++i;
								
								if ( (dump) && ( i % dumpInterval == 0 ) )
								{
									try
									{										
										ImageIO.write(bi, "PNG", new File("out/pic" + currPictureNum + ".png"));
										++currPictureNum;
									} catch (Exception e) {
										e.printStackTrace();
									}
								}							
								
								ga_2.setPCrossover(0.4);
								ga_2.setPMutation(0.3);
								unChangedIterations = 0;
								ga_2.setElitarParentsCount(1);
							}
							else
							{
								++unChangedIterations;
							}
							if ( unChangedIterations > 10 )
							{
								ga_2.setPCrossover( ga_2.getPCrossover() + 0.01 );
								ga_2.setPMutation( ga_2.getPMutation() + 0.05 );
								
								if ( unChangedIterations > 15 )
								{
									ga_2.setElitarParentsCount(0);
								}
							}
							System.out.println(currFit);
						}
					};
					ga_2.addObserver(obs);
					
					for(;;)
					{
						ga_2.iterate(10);
						
						big.clearRect(0, 0, scaledImgWidth, scaledImgHeight);		
						ImageVectorGenetic bestVector_2 = ga_2.getBestFitVector();								
						
						for( ImageVectorGenetic vect : bestVectors )
						{
							vect.scale(scaleCoeff).paint(big);
						}
						
						bestVector_2.scale(scaleCoeff).paint(big);
						
						SwingUtilities.invokeLater(new Runnable(){
							@Override
							public void run() {
								panel.repaint();							
							}
						});
						
						if (nextSchedule)
						{
							bestVectors.add( ga_2.getBestFitVector() );
							break;						
						}
					}					
				}
				System.out.println("-----------DONE-------------");
			}
		});
		t.start();
	}

}
