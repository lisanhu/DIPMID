package org.lsh.images;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.lsh.utils.Utils;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class HSVImage implements Image {
	
	private double[] raster;
	private int w, h, bands = 3;
	
	/**
	 * Constructor of HSVImage
	 * @param src BufferedImage in RGB format
	 */
	public HSVImage(BufferedImage src) {
		if (null == src) {
			src = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
		}
		
		w = src.getWidth();
		h = src.getHeight();
		raster = new double[w * h * bands];
		fromRGBBufferedImage(src);
	}
	
	

	private void fromRGBBufferedImage(BufferedImage src) {
		for (Location loc : new RasterScanner(src, false)) {
			int x = loc.col, y = loc.row;
			int r = src.getRaster().getSample(x, y, 0);
			int g = src.getRaster().getSample(x, y, 1);
			int b = src.getRaster().getSample(x, y, 2);
			double[] hsv = fromRGB(r, g, b);
			setPixel(x, y, hsv);
		}
	}
	
	public BufferedImage toRGBBufferedImage() {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (Location loc : Utils.getRasterScanner(w, h)) {
			int x = loc.col, y = loc.row;
//			int r, g, b;
			int[] rgb = fromHSV(getPixel(x, y));
//			r = rgb[0]; g = rgb[1]; b = rgb[2];
			image.getRaster().setPixel(x, y, rgb);
		}
		return image;
	}
	
	private double[] fromRGB(int r, int g, int b) {
		float[] vals = new float[3];
		double[] result = new double[3];
		Color.RGBtoHSB(r, g, b, vals);
		
		for (int i = 0; i < vals.length; i++) {
			result[i] = vals[i];
		}
		return result;
	}

	private int[] fromHSV(double[] vals) {
		float[] hsv = new float[3];
		for (int i = 0; i < hsv.length; i++) {
			hsv[i] = (float) vals[i];
		}
		int rgb = Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		return new int[]{r, g, b};
	}

	@Override
	public double getSample(int x, int y, int b) {
		return raster[(y * w + x) * bands + b];
	}



	@Override
	public double[] getPixel(int x, int y) {
		double h, s, v;
		h = getSample(x, y, 0);
		s = getSample(x, y, 1);
		v = getSample(x, y, 2);
		return new double[]{h, s, v};
	}



	@Override
	public void setSample(int x, int y, int b, double s) {
		raster[(y * w + x) * bands + b] = s;
	}



	@Override
	public void setPixel(int x, int y, double[] p) {
		for (int b = 0; b < p.length; b++) {
			setSample(x, y, b, p[b]);
		}
	}
}
