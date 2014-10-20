package org.lsh.ops;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public abstract class BinaryImageOp extends NullOp {

	protected BufferedImage left;

	public BinaryImageOp(BufferedImage left) {
		this.left = left;
	}

	protected abstract int combine(int s1, int s2, double alpha);

	public BufferedImage createCompatibleDestImage(Rectangle bounds,
			ColorModel destCM) {
		return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(
				bounds.width, bounds.height), destCM.isAlphaPremultiplied(),
				null);
	}

	@Override
	public BufferedImage filter(BufferedImage right, BufferedImage dest) {
		Rectangle lb = left.getRaster().getBounds(), rb = right.getRaster().getBounds();
		Rectangle inter = lb.intersection(rb);
		int lbands =left.getRaster().getNumBands();
		int rbands = right.getRaster().getNumBands();
		
		if (null == dest) {
			if (lbands < rbands) {
				dest=createCompatibleDestImage(inter, left.getColorModel());
			} else {
				dest=createCompatibleDestImage(inter, right.getColorModel());
			}
		}
		
		for (Location loc : new RasterScanner(dest, true)) {
			int x = loc.col, y = loc.row, b = loc.band;
			int s1 = left.getRaster().getSample(x, y, b);
			int s2 = right.getRaster().getSample(x, y, b);
			dest.getRaster().setSample(x, y, b, combine(s1, s2, 0.5));
		}
		return null;
	}

}
