package org.lsh.color;

import java.awt.Color;

public interface MyColor {
	public MyColor fromNormalColor(Color c);
	public Color toNormalColor();
	
	public double calcNormalL1(Color c);
	public double calcNormalL2(Color c);
}
