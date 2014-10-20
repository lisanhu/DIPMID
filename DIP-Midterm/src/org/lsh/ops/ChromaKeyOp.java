package org.lsh.ops;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import org.lsh.color.HSVColor;
import org.lsh.color.RGBColor;
import org.lsh.color.YIQColor;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class ChromaKeyOp extends BinaryImageOp {

	private BufferedImage left;
	private Color k;
	private double dMin, dMax;
	private boolean metric;
	private String cspace;

	public ChromaKeyOp() {
		this(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB),
				Color.red, 0.3, 0.6, false, "rgb");
	}

	public ChromaKeyOp(BufferedImage left, Color k, double dMin, double dMax,
			boolean metric, String cspace) {
		super(left);
		this.left = left;
		this.k = k;
		this.dMin = dMin;
		this.dMax = dMax;
		this.metric = metric;
		this.cspace = cspace;
	}

	@Override
	public String getAuthorName() {
		return "Sanhu Li";
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage arg0) {
		return new ChromaKeyOp();
	}

	@Override
	public BufferedImage filter(BufferedImage right, BufferedImage dest) {
		Rectangle lb = left.getRaster().getBounds(), rb = right.getRaster()
				.getBounds();
		Rectangle inter = lb.intersection(rb);
		int lbands = left.getRaster().getNumBands();
		int rbands = right.getRaster().getNumBands();

		if (null == dest) {
			if (lbands < rbands) {
				dest = createCompatibleDestImage(inter, left.getColorModel());
			} else {
				dest = createCompatibleDestImage(inter, right.getColorModel());
			}
		}

		for (Location loc : new RasterScanner(dest, true)) {
			int x = loc.col, y = loc.row, bb = loc.band;
			int r = right.getRaster().getSample(x, y, 0);
			int g = right.getRaster().getSample(x, y, 1);
			int b = right.getRaster().getSample(x, y, 2);
			Color c = new Color(r, g, b);
			double alpha = calculateAlpha(c, cspace);
			int s1 = left.getRaster().getSample(x, y, bb);
			int s2 = right.getRaster().getSample(x, y, bb);
			int s = combine(s1, s2, alpha);
			dest.getRaster().setSample(x, y, bb, s);
		}

		return dest;
	}

	private double calculateL1(Color c, String model) {
		if ("rgb".equals(model.toLowerCase())) {
			RGBColor rgb = new RGBColor(k);
			return rgb.calcNormalL1(c);
		} else if ("yiq".equals(model.toLowerCase())) {
			YIQColor yiq = new YIQColor(k);
			return yiq.calcNormalL1(c);
		} else if ("hsb".equals(model.toLowerCase())) {
			HSVColor hsv = new HSVColor(k);
			return hsv.calcNormalL1(c);
		} else {
			throw new RuntimeException("No such color model");
		}
	}

	private double calculateL2(Color c, String model) {
		if ("rgb".equals(model.toLowerCase())) {
			RGBColor rgb = new RGBColor(k);
			return rgb.calcNormalL2(c);
		} else if ("yiq".equals(model.toLowerCase())) {
			YIQColor yiq = new YIQColor(k);
			return yiq.calcNormalL2(c);
		} else if ("hsb".equals(model.toLowerCase())) {
			HSVColor hsv = new HSVColor(k);
			return hsv.calcNormalL2(c);
		} else {
			throw new RuntimeException("No such color model");
		}
	}

	private double calculateAlpha(Color color, String model) {
		double d = metric ? calculateL2(color, model) : calculateL1(color,
				model);
		if (d < dMin) {
			return 0;
		} else if (d >= dMin && d <= dMax) {
			return (d - dMin) / (dMax - dMin);
		} else {
			return 1;
		}
	}

	@Override
	protected int combine(int s1, int s2, double alpha) {
		return (int) (s2 * alpha + s1 * (1 - alpha));
	}

}
