package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MyImage {
	
	String inPath, outPath;
	BufferedImage image;
	BufferedImage convolutionOut;
	
	public MyImage(String inPath, String outPath) {
		super();
		this.inPath = inPath;
		this.outPath = outPath;
		this.image = null;
		getImage();
	}
	
	private void getImage() {
		try {
			this.image = ImageIO.read(new File(inPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Cannot read image at: " + inPath);
			e.printStackTrace();
		}
	}
	
	public void saveImage() {
		File output = new File(outPath);
		try {
			ImageIO.write(image, "bmp", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Cannot save image at: " +outPath);
			e.printStackTrace();
		}
	}
	
	public void convolution(float[][] kernel) {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				float red = 0, green = 0, blue =0, alpha = 0;
				for (int filterY = 0; filterY < kernel[0].length; filterY++) {
					for (int filterX = 0; filterX < kernel.length; filterX++) {
					      int imageX = (x - kernel.length / 2 + filterX + image.getWidth()) % image.getWidth();
					      int imageY = (y - kernel[0].length / 2 + filterY + image.getHeight()) % image.getHeight();
					      alpha += ((image.getRGB(imageX, imageY) >> 24) & 0xFF) * kernel[filterY][filterX];
					      red += ((image.getRGB(imageX, imageY) >> 16) & 0xFF) * kernel[filterY][filterX];
					      green += ((image.getRGB(imageX, imageY) >> 8) & 0xFF) * kernel[filterY][filterX];
					      blue += (image.getRGB(imageX, imageY) & 0xFF) * kernel[filterY][filterX];
					}
				}
				alpha = (alpha < 0 ? 0 : alpha);
				alpha = (alpha > 255 ? 255 : alpha);
				red = (red < 0 ? 0 : red);
				red = (red > 255 ? 255 : red);
				green = (green < 0 ? 0 : green);
				green = (green > 255 ? 255 : green);
				blue = (blue < 0 ? 0 : blue);
				blue = (blue > 255 ? 255 : blue);
				
				result.setRGB(x, y, (int)blue + ((int)green << 8) + ((int)red << 16) + ((int)alpha << 24));
				
			}
		}
		
		image = result;
	}
	

	public float[][] boxKernel(int size) {
		if(size % 2 == 0)
			size++;
		int factor = size*size;
		float[][] kernel = new float[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				kernel[i][j] = 1.0f / factor;
			}
		}
		return kernel;
	}
	
	public float[][] boxWeightKernel(int size)  {
		float[][] kernel = new float[3][3];
		float factor = 16f;
		kernel[0][0] = 1.0f/factor; kernel[0][1] = 2.0f/factor; kernel[0][2] = 1.0f/factor;
		kernel[1][0] = 2.0f/factor; kernel[1][1] = 4.0f/factor; kernel[1][2] = 2.0f/factor;
		kernel[2][0] = 1.0f/factor; kernel[2][1] = 2.0f/factor; kernel[2][2] = 1.0f/factor;
		return kernel;
	}
	
	public float[][] gaussianKernel(int sigma, float radius) {
		int r = (int)Math.ceil(radius);
		int rows = r*2+1;
		float[][] kernel = new float[rows][rows];
		float sigma22 = 2*sigma*sigma;
		float sigmaPi2 = 2*(float)Math.PI*sigma;
		float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2); 
		
		return kernel;
	}
	
	public void paddImageZero(int size)
	{
		BufferedImage newImage = new BufferedImage(image.getWidth()+2*size, image.getHeight()+2*size, BufferedImage.TYPE_INT_RGB);
		for(int i =0; i<newImage.getWidth(); i++)
			for (int j = 0; j < newImage.getHeight(); j++) {
				newImage.setRGB(i, j, 255);
				if(i>size && j>size && i<newImage.getWidth() -size && j<newImage.getHeight() -size ){
					newImage.setRGB(i, j, image.getRGB(i -size, j-size));
				}
			}
		image = newImage;
	}
	
}
