package org.lsh.ops;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import org.lsh.images.HSVImage;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class ImageSaturationOp extends NullOp {

	int orientation;

	public ImageSaturationOp() {
		this(0);
	}

	public ImageSaturationOp(int orientation) {
		switch (orientation) {
		case 0:
		case 1:
		case 2:
			this.orientation = orientation;
			break;
		default:
			throw new RuntimeException("Error orientation value " + orientation);
		}
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage paramBufferedImage) {
		return new ImageSaturationOp();
	}

	@Override
	public String getAuthorName() {
		return "Sanhu Li";
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		HSVImage hsv = new HSVImage(src);
		for (Location loc : new RasterScanner(src, false)) {
			int x = loc.col, y = loc.row;
			double s = hsv.getSample(x, y, 1);
			s = calculateS(src, x, y, s);
			hsv.setSample(x, y, 1, s);
		}

		dest = hsv.toRGBBufferedImage();
		return dest;
	}

	private double calculateS(BufferedImage src, int x, int y, double s) {
		switch (orientation) {
		case 0:
			return x / (double) src.getWidth();
		case 1:
			return 1 - y / (double) src.getHeight();
		case 2:
			int mid_w = src.getWidth() / 2,
			mid_h = src.getHeight() / 2;
			double total_d = calculateDistance(0, 0, mid_w, mid_h);
			double d = calculateDistance(x, y, mid_w, mid_h);
			return 1 - d / total_d;
		default:
			throw new RuntimeException("Error orientation value " + orientation);
		}
	}

	private double calculateDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
}
