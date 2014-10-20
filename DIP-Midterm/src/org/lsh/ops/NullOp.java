package org.lsh.ops;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import pixeljelly.ops.PluggableImageOp;

public abstract class NullOp implements PluggableImageOp {

	@Override
	public Rectangle2D getBounds2D(BufferedImage src) {
		return src.getRaster().getBounds();
	}

	@Override
	public BufferedImage createCompatibleDestImage(BufferedImage src,
			ColorModel destCM) {
		int w = src.getWidth(), h = src.getHeight();
		return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(
				w, h), destCM.isAlphaPremultiplied(), null);
	}

	@Override
	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		return null;
	}

	@Override
	public RenderingHints getRenderingHints() {
		return getRenderingHints();
	}

}
