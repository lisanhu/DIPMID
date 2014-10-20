package org.lsh.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lsh.images.HSVImage;
import org.lsh.ops.ChromaKeyOp;

public class Test {

	public static void main(String[] args) throws IOException {
//		String src = "input/src.jpg", out = "output/out.png";
//		BufferedImage image = ImageIO.read(new File(src));
//		HSVImage hsv = new HSVImage(image);
//		BufferedImage outim = hsv.toRGBBufferedImage();
//		ImageIO.write(outim, "png", new File(out));
		String left = "input/room.jpg", right = "input/girl.jpg", out = "output/out.jpg";
		BufferedImage limage = ImageIO.read(new File(left));
		BufferedImage rImage = ImageIO.read(new File(right));
		ChromaKeyOp op = new ChromaKeyOp(limage, new Color(93, 232, 117), 0.2, 0.23, true, "hsb");
		BufferedImage dest = op.filter(rImage, null);
		ImageIO.write(dest, "jpg", new File(out));
	}

}
