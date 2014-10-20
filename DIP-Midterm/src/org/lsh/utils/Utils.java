package org.lsh.utils;

import java.awt.Rectangle;

import pixeljelly.scanners.RasterScanner;

public class Utils {

	public static RasterScanner getRasterScanner(int width, int height) {
		return new RasterScanner(new Rectangle(width, height));
	}
}
