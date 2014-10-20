package org.lsh.ops;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import org.lsh.images.HSVImage;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class HueMagnetOp extends NullOp {

	double hm, sensitivity;

	public HueMagnetOp() {
		this(0.5, 2);
	}

	public HueMagnetOp(double hm, double sensitivity) {

		if (!(hm >= 0 && hm <= 1)) {
			throw new RuntimeException("Hue is " + hm + " not in range [0, 1]");
		}

		if (!(sensitivity >= 1 && sensitivity <= 10)) {
			throw new RuntimeException("Sensitivity is " + sensitivity
					+ " not in range [1, 10]");
		}

		this.hm = hm;
		this.sensitivity = sensitivity;
	}

	/**
	 * Calculate the smallest angle from h1 to h2 h1 and h2 using 1 represent
	 * 360°
	 * 
	 * @param h1
	 * @param h2
	 * @return
	 */
	private double angle(double h1, double h2) {
		while (h1 > 1)
			h1 -= 1;
		while (h1 < 0)
			h1 += 1;
		while (h2 > 1)
			h2 -= 1;
		while (h2 < 0)
			h2 += 1;
		
		double diff = Math.abs(h2 - h1);
		
		if (diff > .5) {
			return 1 - diff;
		} else {
			return diff;
		}
	}

	private int getSign(double h) {
		if ((h > hm && angle(h, hm) < .5) || hm >= h && angle(h, hm) >= .5) {
			return -1;
		} else if ((h > hm && angle(h, hm) >= .5) || hm >= h
				&& angle(h, hm) < .5) {
			return 1;
		} else {
			return 0;
		}
	}

	private double calculateH(double h) {
		return h + Math.pow(angle(h, hm), sensitivity) * getSign(h);
	}

	@Override
	public String getAuthorName() {
		return "Sanhu Li";
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage arg0) {
		return new HueMagnetOp();
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		HSVImage hsv = new HSVImage(src);
		for (Location loc : new RasterScanner(src, false)) {
			int x = loc.col, y = loc.row;
			double h = hsv.getSample(x, y, 0);
			h = calculateH(h);
			hsv.setSample(x, y, 0, h);
		}
		dest = hsv.toRGBBufferedImage();
		return dest;
	}

	public double getHm() {
		return hm;
	}

	public void setHm(double hm) {
		this.hm = hm;
	}

	public double getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(double sensitivity) {
		this.sensitivity = sensitivity;
	}

}
