package org.lsh.color;

import java.awt.Color;
import static java.lang.Math.*;

public class HSVColor implements MyColor {

	private double h, s, v;

	public HSVColor() {
		h = s = v = 0;
	}

	public HSVColor(Color color) {
		setColor(color);
	}

	private void setColor(Color color) {
		float[] vals = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), vals);
		h = vals[0];
		s = vals[1];
		v = vals[2];
	}

	@Override
	public HSVColor fromNormalColor(Color c) {
		setColor(c);
		return this;
	}

	@Override
	public Color toNormalColor() {
		int rgb;
		rgb = Color.HSBtoRGB((float) h, (float) s, (float) v);
		return new Color(rgb);
	}

	@Override
	public double calcNormalL1(Color c) {
		HSVColor cc = new HSVColor(c);
		double hh, ss, vv;
		hh = abs(s * cos(2 * PI * h) - cc.s * cos(2 * PI * cc.h));
		ss = abs(s * sin(2 * PI * h) - cc.s * sin(2 * PI * cc.h));
		vv = abs(v - cc.v);
		double d = hh + ss + vv;
		return d / (1 + sqrt(2));
	}

	@Override
	public double calcNormalL2(Color c) {
		HSVColor cc = new HSVColor(c);
		double hh, ss, vv;
		hh = abs(s * cos(2 * PI * h) - cc.s * cos(2 * PI * cc.h));
		ss = abs(s * sin(2 * PI * h) - cc.s * sin(2 * PI * cc.h));
		vv = abs(v - cc.v);
		double d = sqrt(hh * hh + ss * ss + vv * vv);
		return d / sqrt(5);
	}

}
