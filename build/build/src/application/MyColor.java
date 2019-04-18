package application;

public class MyColor {
	int red, green, blue;
	
	public MyColor(int r, int g, int b) {
		red = r;
		green =g;
		blue = b;
	}

	public MyColor(int rgb) {
		red = (rgb >> 16) & 0xFF;
		green = (rgb >>8 ) & 0xFF;
		blue = (rgb) & 0xFF;
	}
	
	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}
	
	public int getRGB() {
		return blue + green << 8 + red << 16;
	}
	
}
