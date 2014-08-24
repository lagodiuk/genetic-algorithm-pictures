package optimization.painting.utils;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class Copy_2_of_ImageSimilarity {
	
	private int nThreads = 2;
	private ExecutorService executor = Executors.newFixedThreadPool(nThreads + 1);
	
	public abstract double distanceRGB ( int r1, int g1, int b1, int r2, int g2, int b2 );
	
	public double difference( BufferedImage img1, BufferedImage img2 ) {
		double ret = 0;
		
		final WritableRaster r1 = img1.getRaster();
		final WritableRaster r2 = img2.getRaster();

		List<Callable<Double>> tasks = new LinkedList<Callable<Double>>();
		
		final int d = r1.getWidth()/nThreads;
		int curr = 0;
		
		for( int i = 0; i < nThreads; i++) {
			final int _curr = curr;
			tasks.add(new Callable<Double>(){
				@Override
				public Double call() throws Exception {					
					return partialDiff(r1, r2, _curr, _curr + d - 1);
				}
			});
			curr += d;
		}
		if ( curr < r1.getWidth() )
		{
			final int _curr = curr;
			tasks.add(new Callable<Double>(){
				@Override
				public Double call() throws Exception {					
					return partialDiff(r1, r2, _curr, r1.getWidth());
				}
			});
		}
		
		try
		{
			List<Future<Double>> results = executor.invokeAll(tasks);
			for( Future<Double> f : results )
			{
				ret += f.get();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private Double partialDiff( WritableRaster r1, WritableRaster r2, int x0, int x1) {
		double ret = 0;
		
		int[] rgb1 = new int[3];
		int[] rgb2 = new int[3];
		
		for ( int i = x0; i < x1; i ++ )
		{
			for ( int j = 0; j < r1.getHeight(); j ++ )
			{				
				rgb1 = r1.getPixel( i, j, rgb1 );
				rgb2 = r2.getPixel( i, j, rgb2 );				
				
				ret += distanceRGB( rgb1[0], rgb1[1], rgb1[2], rgb2[0], rgb2[1], rgb2[2]);
			}
		}
		
		return ret;
	}
	
}
