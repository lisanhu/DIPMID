package org.lsh.color;

import java.awt.Color;
import static java.lang.Math.*;

public class YIQColor implements MyColor {

	private double y, i, q;

	public YIQColor() {
		y = 0;
		i = 0;
		q = 0;
	}

	public YIQColor(Color color) {
		setColor(color);
	}

	private void setColor(Color c) {
		double r = c.getRed() / 255d, g = c.getGreen() / 255d, b = c.getBlue() / 255d;
		y = .299 * r + .587 * g + .114 * b;
		i = .5957 * r - .2745 * g - .3212 * b;
		q = .2115 * r - .5226 * g + .3111 * b;
	}

	@Override
	public YIQColor fromNormalColor(Color c) {
		setColor(c);
		return this;
	}

	@Override
	public Color toNormalColor() {
		double r, g, b;
		r = 1 * y + .9563 * i + .6210 * q;
		g = 1 * y - .2721 * i - .6474 * q;
		b = 1 * y - 1.1070 * i + 1.7046 * q;
		return new Color((float) r, (float) g, (float) b);
	}

	@Override
	public double calcNormalL1(Color c) {
		YIQColor cc = new YIQColor(c);
		double yy, ii, qq;
		yy = abs(y - cc.y);
		ii = abs(i - cc.i);
		qq = abs(q - cc.q);
		double d = yy + ii + qq;
		return d / 3.2366;
	}

	@Override
	public double calcNormalL2(Color c) {
		YIQColor cc = new YIQColor(c);
		double yy, ii, qq;
		yy = abs(y - cc.y);
		ii = abs(i - cc.i);
		qq = abs(q - cc.q);
		double d = sqrt(yy * yy + ii * ii + qq * qq);
		return d / 1.874;
	}

}
