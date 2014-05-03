package s3.util;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class TinterComposite implements Composite {
	TinterCompositeContext tcc;

	public TinterComposite(float r,float g,float b) {
		tcc = new TinterCompositeContext(r,g,b);
	}

	public CompositeContext createContext(ColorModel arg0, ColorModel arg1,
			RenderingHints arg2) {
		return tcc;
	}

	class TinterCompositeContext implements CompositeContext {
		float m_r,m_g,m_b;

		public TinterCompositeContext(float r,float g,float b) {
			m_r = r;
			m_g = g;
			m_b = b;
		}

		public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
			int[] srcPixel = new int[4];
			int[] dstPixel = new int[4];

			for (int y = dstIn.getMinY(); y < dstIn.getMinY() + dstIn.getHeight(); y++) {
				for (int x = dstIn.getMinX(); x < dstIn.getMinX() + dstIn.getWidth(); x++) {
					srcPixel = src.getPixel(x, y, srcPixel);
					dstPixel[0] = ((int)(srcPixel[0] * m_r)) & 0xff;
					dstPixel[1] = ((int)(srcPixel[1] * m_g)) & 0xff;
					dstPixel[2] = ((int)(srcPixel[2] * m_b)) & 0xff;
					dstPixel[3] = srcPixel[3];

					dstOut.setPixel(x, y, dstPixel);
				}
			}
		}

		public void dispose() {
		}

	}

}
