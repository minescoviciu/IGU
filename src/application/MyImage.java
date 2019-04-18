package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

public class MyImage{
	
	String inPath, outPath;
	BufferedImage image;
	BufferedImage origImage;
	
	public BufferedImage getBufferedImage() {
		return image;
	}
	
	public BufferedImage getOrigBufferedImage() {
		return origImage;
	}
	
	public MyImage(String inPath, String outPath) {
		super();
		this.inPath = inPath;
		this.outPath = outPath;
		this.image = null;
		this.origImage = null;
		getImage();
	}
	
	private void getImage() {
		try {
			this.image = ImageIO.read(new File(inPath));
			this.origImage = ImageIO.read(new File(inPath));
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
	
//	Service convolutionService =  new Service() {
//
//		@Override
//		protected Task createTask() {
//			// TODO Auto-generated method stub
//			return new Task<Void>() {
//
//				@Override
//				protected Void call() throws Exception {
//					// TODO Auto-generated method stub
//					BufferedImage result = new BufferedImage(origImage.getWidth(), origImage.getHeight(), BufferedImage.TYPE_INT_RGB);
//					for(int x = 0; x < origImage.getWidth(); x++) {
//						double p = (double)x/(double)origImage.getWidth();
//						updateProgress(p,1);
////						System.out.println(p);
//						for (int y = 0; y < origImage.getHeight(); y++) {
//							float red = 0, green = 0, blue =0, alpha = 0;
//							for (int filterY = 0; filterY < kernelTask[0].length; filterY++) {
//								for (int filterX = 0; filterX < kernelTask.length; filterX++) {
//								      int imageX = (x - kernelTask.length / 2 + filterX + origImage.getWidth()) % origImage.getWidth();
//								      int imageY = (y - kernelTask[0].length / 2 + filterY + origImage.getHeight()) % origImage.getHeight();
//								      alpha += ((origImage.getRGB(imageX, imageY) >> 24) & 0xFF) * kernelTask[filterY][filterX];
//								      red += ((origImage.getRGB(imageX, imageY) >> 16) & 0xFF) * kernelTask[filterY][filterX];
//								      green += ((origImage.getRGB(imageX, imageY) >> 8) & 0xFF) * kernelTask[filterY][filterX];
//								      blue += (origImage.getRGB(imageX, imageY) & 0xFF) * kernelTask[filterY][filterX];
//								}
//							}
//							alpha = (alpha < 0 ? 0 : alpha);
//							alpha = (alpha > 255 ? 255 : alpha);
//							red = (red < 0 ? 0 : red);
//							red = (red > 255 ? 255 : red);
//							green = (green < 0 ? 0 : green);
//							green = (green > 255 ? 255 : green);
//							blue = (blue < 0 ? 0 : blue);
//							blue = (blue > 255 ? 255 : blue);
//							
//							result.setRGB(x, y, (int)blue + ((int)green << 8) + ((int)red << 16) + ((int)alpha << 24));
//							
//						}
//					}
//					updateProgress(1, 1);
//					image = result;
//					return null;
//				};
//			};
//		}
//	};
//	
	
	public Task<Void> convolutionTask()
	{
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				BufferedImage result = new BufferedImage(origImage.getWidth(), origImage.getHeight(), BufferedImage.TYPE_INT_RGB);
				for(int x = 0; x < origImage.getWidth(); x++) {
					double p = (double)x/(double)origImage.getWidth();
					updateProgress(p,1);
					//System.out.println(p);
					for (int y = 0; y < origImage.getHeight(); y++) {
						float red = 0, green = 0, blue =0, alpha = 0;
						for (int filterY = 0; filterY < kernelTask[0].length; filterY++) {
							for (int filterX = 0; filterX < kernelTask.length; filterX++) {
							      int imageX = (x - kernelTask.length / 2 + filterX + origImage.getWidth()) % origImage.getWidth();
							      int imageY = (y - kernelTask[0].length / 2 + filterY + origImage.getHeight()) % origImage.getHeight();
							      alpha += ((origImage.getRGB(imageX, imageY) >> 24) & 0xFF) * kernelTask[filterY][filterX];
							      red += ((origImage.getRGB(imageX, imageY) >> 16) & 0xFF) * kernelTask[filterY][filterX];
							      green += ((origImage.getRGB(imageX, imageY) >> 8) & 0xFF) * kernelTask[filterY][filterX];
							      blue += (origImage.getRGB(imageX, imageY) & 0xFF) * kernelTask[filterY][filterX];
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
				updateProgress(1, 1);
				image = result;
				return null;
			}
			
		};
	}

	private float[][] kernelTask;
	
	public void setKernelTask(float[][] kernel) {
		this.kernelTask = kernel;
	}
	
	public void convolution(float[][] kernel) {
		BufferedImage result = new BufferedImage(origImage.getWidth(), origImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < origImage.getWidth(); x++) {
			for (int y = 0; y < origImage.getHeight(); y++) {
				float red = 0, green = 0, blue =0, alpha = 0;
				for (int filterY = 0; filterY < kernel[0].length; filterY++) {
					for (int filterX = 0; filterX < kernel.length; filterX++) {
					      int imageX = (x - kernel.length / 2 + filterX + origImage.getWidth()) % origImage.getWidth();
					      int imageY = (y - kernel[0].length / 2 + filterY + origImage.getHeight()) % origImage.getHeight();
					      alpha += ((origImage.getRGB(imageX, imageY) >> 24) & 0xFF) * kernel[filterY][filterX];
					      red += ((origImage.getRGB(imageX, imageY) >> 16) & 0xFF) * kernel[filterY][filterX];
					      green += ((origImage.getRGB(imageX, imageY) >> 8) & 0xFF) * kernel[filterY][filterX];
					      blue += (origImage.getRGB(imageX, imageY) & 0xFF) * kernel[filterY][filterX];
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
	

	public static float[][] boxKernel(int size) {
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
	
	public static float[][] boxWeightKernel(int size)  {
		float[][] kernel = new float[3][3];
		float factor = 16f;
		kernel[0][0] = 1.0f/factor; kernel[0][1] = 2.0f/factor; kernel[0][2] = 1.0f/factor;
		kernel[1][0] = 2.0f/factor; kernel[1][1] = 4.0f/factor; kernel[1][2] = 2.0f/factor;
		kernel[2][0] = 1.0f/factor; kernel[2][1] = 2.0f/factor; kernel[2][2] = 1.0f/factor;
		return kernel;
	}
	
//	public float[][] gaussianKernel(int sigma, int size) {
//		float[][] kernel = new float[size][size];
//		float sigma22 = 2*sigma*sigma;
//		float sigmaPi2 = 2*(float)Math.PI*sigma22;
//		float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2); 
//		float constant = 1/ sqrtSigmaPi2;
//		for (int i = 0; i < kernel.length; i++) {
//			for (int j = 0; j < kernel[0].length; j++) {
//				kernel[i][j] = constant * (float) Math.exp((i*i + j*j)/(-2*sigma22));
//			}
//		}
//		return kernel;
//	}
//	
	public static float[][] gaussianKernel(float sigma, float radius) {
		int r = (int)Math.ceil(radius);
		int rows = r*2+1;
		double[][] matrix = new double[rows][rows];
		float[][] kernel = new float[rows][rows];
		double sigma22 = 2*sigma*sigma;
		double sigmaPi2 = (2*Math.PI*sigma22);
		double total = 0;
		for (int i = -r; i <= r; i++) {
			for (int j = -r; j <= r; j++) {
				double z = Math.sqrt(i*i + j*j);
				matrix[i+r][j+r] = Math.exp(-(z*z) / sigma22) / sigmaPi2;
				total += matrix[i+r][j+r];
			}
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] /= total;
				kernel[i][j] = (float)matrix[i][j];
			}
		}

		return kernel;
	}
	
	public void paddImageZero(int size)
	{
		BufferedImage newImage = new BufferedImage(image.getWidth()+2*size, image.getHeight()+2*size,
										BufferedImage.TYPE_INT_RGB);
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
