package org.lsh.color;

import java.awt.Color;
import static java.lang.Math.*;

public class RGBColor implements MyColor {

	double r, g, b;

	public RGBColor() {
		r = g = b = 0;
	}

	public RGBColor(Color color) {
		setColor(color);
	}

	private void setColor(Color color) {
		r = color.getRed() / 255d;
		g = color.getGreen() / 255d;
		b = color.getBlue() / 255d;
	}

	@Override
	public RGBColor fromNormalColor(Color c) {
		setColor(c);
		return this;
	}

	@Override
	public Color toNormalColor() {
		int rr = (int) (r * 255);
		int gg = (int) (g * 255);
		int bb = (int) (b * 255);
		return new Color(rr, gg, bb);
	}

	@Override
	public double calcNormalL1(Color c) {
		RGBColor cc = new RGBColor(c);
		double dr = abs(cc.r - r), dg = abs(cc.g - g), db = abs(cc.b - b);
		double d = dr + dg + db;
		return d / 3;
	}

	@Override
	public double calcNormalL2(Color c) {
		RGBColor cc = new RGBColor(c);
		double dr = abs(cc.r - r), dg = abs(cc.g - g), db = abs(cc.b - b);
		double d = sqrt(dr * dr + dg * dg + db * db);
		return d / sqrt(3);
	}

}
