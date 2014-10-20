package org.lsh.images;

public interface Image {
	public double getSample(int x, int y, int b);
	public double[] getPixel(int x, int y);
	
	public void setSample(int x, int y, int b, double s);
	public void setPixel(int x, int y, double[] p);
}
