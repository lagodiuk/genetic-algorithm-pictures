import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Test {

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("Test");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(300, 300);
		
		final BufferedImage bi = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
		Graphics2D big = bi.createGraphics();
		
		ImageIcon ico = new ImageIcon("firefox.png");
		//ImageIO.read("firefox.png");
		big.drawImage(ico.getImage(), 0, 0, null);
		
		big.setColor( new Color(200, 250, 50, 200) );
		big.fillRect(10, 10, 25, 52);
		
		int[] rgb = new int[3];
		bi.getRaster().getPixel( 12, 12, rgb );
		System.out.println("b = " + rgb[2] );
		System.out.println("g = " + rgb[1] );
		System.out.println("r = " + rgb[0] );		
		
		//big.clearRect(0, 0, 30, 30);
		
		mainFrame.getContentPane().add( new JPanel()		
		{
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.drawImage(bi, 0, 0, null);
			}
		});
		
		mainFrame.setVisible(true);
	}
	
}
