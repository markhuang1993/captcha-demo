package com.mark;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageUtil {

    public static BufferedImage filterScale(BufferedImage img, float ratio) {
        final int w = (int) (ratio * img.getWidth());
        final int h = (int) (ratio * img.getHeight());
        final BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                final int rgb = img.getRGB((int) (x / ratio), (int) (y / ratio));
                newImg.setRGB(x, y, rgb);
            }
        }
        return newImg;
    }

    public static BufferedImage filterThreshold(BufferedImage img, int threshold) {
        final int w = img.getWidth();
        final int h = img.getHeight();
        final BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Color min = Color.BLACK;
        Color max = Color.WHITE;
        if (threshold < 0) {
            min = Color.WHITE;
            max = Color.BLACK;
            threshold = -threshold;
        }
        threshold *= 3;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                final Color color = new Color(img.getRGB(x, y));
                if (color.getRed() + color.getGreen() + color.getBlue() < threshold) {
                    newImg.setRGB(x, y, min.getRGB());
                } else {
                    newImg.setRGB(x, y, max.getRGB());
                }
            }
        }
        return newImg;
    }

    public static BufferedImage filterNoiseLine(BufferedImage img) {
        final int w = img.getWidth();
        final int h = img.getHeight();
        final BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                newImg.setRGB(x, y, img.getRGB(x, y));
            }
        }

        List<LineX> lineXList = new ArrayList<>();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Point tempP = new Point(x, y);
                List<Point> points = new ArrayList<>();
                points.add(tempP);
                for (int r = x; r < w - x; r++) {
                    Color c = new Color(img.getRGB(tempP.x, tempP.y));

                    findP:
                    for (int ofY = 0; ofY < Math.min(2, h - y); ofY++) {
                        for (int ofX = 0; ofX < Math.min(2, w - r); ofX++) {
                            int nx = r + ofX;
                            int ny = y + ofY;
                            Color c2 = new Color(img.getRGB(nx, ny));

                            if (similarTo(c, c2)) {
                                points.add(new Point(nx, ny));
                                tempP = new Point(nx, ny);
                                r = nx;
                                break findP;
                            }
                        }
                    }
                }

                if (points.size() > 0) {
                    if (points.size() > 300) {
                        lineXList.add(new LineX(x, tempP.x, y, points));
                    }
                    x = tempP.x;
                }
            }
        }

        for (final LineX lineX : lineXList) {
            for (final Point point : lineX.points) {
                newImg.setRGB(point.x, point.y, Color.white.getRGB());
            }
        }

        return newImg;
    }

	public static boolean similarTo(Color c1, Color c2) {
		int red1 = c1.getRed();
		int green1 = c1.getGreen();
		int blue1 = c1.getBlue();

		int red2 = c2.getRed();
		int green2 = c2.getGreen();
		int blue2 = c2.getBlue();
		long distance = (red1 - red2) * (red1 - red2) +
				(green1 - green2) * (green1 - green2) +
				(blue1 - blue2) * (blue1 - blue2);
		return distance < 150 || (Math.abs(red1 - red2) < 5 || Math.abs(green1 - green2) < 5 || Math.abs(blue1 - blue2) < 5);
	}

    public static class LineX {
        int startX;
        int endX;
        int y;
        List<Point> points = new ArrayList<>();

        public LineX(final int startX, final int endX, final int y, final List<Point> points) {
            this.startX = startX;
            this.endX = endX;
            this.y = y;
            this.points = points;
        }
    }

    public static class Point {
        int x;
        int y;

        public Point(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static BufferedImage filterSmooth(BufferedImage img) throws Exception {
        final int SSIZE = 3;
        int x, y, i, j, val;
        int r;
        final int w = img.getWidth();
        final int h = img.getHeight();
        final BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                newImg.setRGB(x, y, img.getRGB(x, y));
            }
        }

        for (y = SSIZE / 2; y < h - SSIZE / 2; y++) {
            for (x = SSIZE / 2; x < w - SSIZE / 2; x++) {
                val = 0;
                for (i = 0; i < SSIZE; i++) {
                    for (j = 0; j < SSIZE; j++) {
                        final Color color = new Color(img.getRGB(x + j - SSIZE / 2, y + i - SSIZE / 2));
                        r = color.getRed();
                        val += r;
                    }
                }
                i = val / (SSIZE * SSIZE);
                newImg.setRGB(x, y, new Color(i, i, i).getRGB());
            }
        }
        /* Remove border */
        for (y = 0; y < h; y++) {
            newImg.setRGB(0, y, newImg.getRGB(1, y));
            newImg.setRGB(w - 1, y, newImg.getRGB(w - 2, y));
        }

        for (x = 0; x < w; x++) {
            newImg.setRGB(x, 0, newImg.getRGB(x, 1));
            newImg.setRGB(x, h - 1, newImg.getRGB(x, h - 2));
        }

        return newImg;
    }

    public static HashMap<BufferedImage, FontGlyphs> loadFontFixed(String fontFile, String fontText) throws Exception {
        final int fontCount = fontText.length();
        final BufferedImage img = ImageIO.read(new File(fontFile));
        final int w = img.getWidth() / fontCount;
        final int h = img.getHeight();
        final HashMap<BufferedImage, FontGlyphs> fontMap = new HashMap<BufferedImage, FontGlyphs>();
        for (int i = 0; i < fontCount; i++) {
            fontMap.put(img.getSubimage(w * i, 0, w, h), new FontGlyphs(fontText.charAt(i) + "", 0));
        }
        return fontMap;
    }

    public static HashMap<BufferedImage, FontGlyphs> loadFontVariable(String fontFile, String fontText)
            throws Exception {
        final BufferedImage img = ImageIO.read(new File(fontFile));
        final int w = img.getWidth();
        final int h = img.getHeight();
        final HashMap<BufferedImage, FontGlyphs> fontMap = new HashMap<BufferedImage, FontGlyphs>();
        int found = 0;
        int incell = 0;
        int xmin = 0;
        int i = 0;
        int weight = 0;
        for (int x = 0; x < w; ++x) {
            found = 0;
            for (int y = 0; y < h; ++y) {
                final int r = new Color(img.getRGB(x, y)).getRed();
                if (r < 250) {
                    found = 1;
                    weight += (255 - r);
                }
            }
            if (found == 1 && incell == 0) {
                incell = 1;
                xmin = x;
            } else if (found == 0 && incell == 1) {
                incell = 0;
                fontMap.put(img.getSubimage(xmin, 0, x - xmin, h), new FontGlyphs(fontText.charAt(i) + "", weight));
                weight = 0;
                i++;
            }
        }
        return fontMap;
    }

    public static BufferedImage filterDetectLines(BufferedImage img) {

        int x, y;
        int r, ra, rb;
        final int w = img.getWidth();
        final int h = img.getHeight();
        final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        /* Remove white lines */
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                r = new Color(img.getRGB(x, y)).getRed();
//                dst.setRGB(x, y, img.getRGB(x, y));
                if (y > 0 && y < h - 1) {
                    ra = new Color(img.getRGB(x, y - 1)).getRed();
                    rb = new Color(img.getRGB(x, y + 1)).getRed();
                    if (r > ra && (r - ra) * (r - rb) > 5000) {
                        dst.setRGB(x, y, new Color(ra, ra, ra).getRGB());
                    }
                }
            }
        }

        /* Remove black lines */
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                r = new Color(img.getRGB(x, y)).getRed();
                if (y > 0 && y < h - 1) {
                    ra = new Color(img.getRGB(x, y - 1)).getRed();
                    rb = new Color(img.getRGB(x, y + 1)).getRed();
                    if (r < ra && (r - ra) * (r - rb) > 500) {
                        dst.setRGB(x, y, new Color(ra, ra, ra).getRGB());
                    }
                }
            }
        }
        return dst;
    }

    public static BufferedImage filterFillHoles(BufferedImage img) {
        int x, y;

        final int w = img.getWidth();
        final int h = img.getHeight();
        final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                dst.setRGB(x, y, img.getRGB(x, y));
            }
        }
        for (y = 0; y < h; y++) {
            for (x = 2; x < w - 2; x++) {
                int c1, c2, c3, c4, c5;
                c1 = new Color(img.getRGB(x - 2, y)).getRed();
                c2 = new Color(img.getRGB(x - 1, y)).getRed();
                c3 = new Color(img.getRGB(x, y)).getRed();
                c4 = new Color(img.getRGB(x + 1, y)).getRed();
                c5 = new Color(img.getRGB(x + 2, y)).getRed();

                if (c1 < 127 && c2 < 127 && c3 > 128 && c4 < 127) {
                    c3 = (c1 + c2 + c4) / 3;
                } else if (c2 < 127 && c3 > 128 && c4 < 127 && c5 < 127) {
                    c3 = (c2 + c4 + c5) / 3;
                }
                dst.setRGB(x, y, new Color(c3, c3, c3).getRGB());
            }
        }
        for (x = 0; x < w; x++) {
            for (y = 2; y < h - 2; y++) {
                int c1, c2, c3, c4, c5;
                c1 = new Color(img.getRGB(x, y - 2)).getRed();
                c2 = new Color(img.getRGB(x, y - 1)).getRed();
                c3 = new Color(img.getRGB(x, y)).getRed();
                c4 = new Color(img.getRGB(x, y + 1)).getRed();
                c5 = new Color(img.getRGB(x, y + 2)).getRed();

                if (c1 < 127 && c2 < 127 && c3 > 128 && c4 < 127) {
                    c3 = (c1 + c2 + c4) / 3;
                } else if (c2 < 127 && c3 > 128 && c4 < 127 && c5 < 127) {
                    c3 = (c2 + c4 + c5) / 3;
                }
                dst.setRGB(x, y, new Color(c3, c3, c3).getRGB());
            }
        }
        return dst;

    }

    public static BufferedImage filterMedian(BufferedImage img) {
        final int MSIZE = 3;

        int x, y, i, j;
        int r;
        final int[] val = new int[MSIZE * MSIZE];
        final int w = img.getWidth();
        final int h = img.getHeight();
        final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                dst.setRGB(x, y, Color.WHITE.getRGB());
            }
        }

        for (y = MSIZE / 2; y < h - MSIZE / 2; y++) {
            for (x = MSIZE / 2; x < w - MSIZE / 2; x++) {
                for (i = 0; i < MSIZE; i++) {
                    for (j = 0; j < MSIZE; j++) {
                        r = new Color(img.getRGB(x + j - MSIZE / 2, y + i - MSIZE / 2)).getRed();
                        val[i * MSIZE + j] = r;
                    }
                }
                /* Bubble sort power! */
                for (i = 0; i < MSIZE * MSIZE / 2 + 1; i++) {
                    for (j = i + 1; j < MSIZE * MSIZE; j++) {
                        if (val[i] > val[j]) {
                            final int k = val[i];
                            val[i] = val[j];
                            val[j] = k;
                        }
                    }
                }
                i = val[MSIZE * MSIZE / 2];
                dst.setRGB(x, y, new Color(i, i, i).getRGB());
            }
        }
        return dst;
    }

    public static BufferedImage filterContrast(BufferedImage img) {
        final int[] histo = new int[256];
        int x, y, i, min = 255, max = 0;
        int r;
        final int w = img.getWidth();
        final int h = img.getHeight();
        final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                r = new Color(img.getRGB(x, y)).getGreen();
                if (r < min) {
                    min = r;
                }
                if (r > max) {
                    max = r;
                }
            }
        }
        if (min == max) {
            histo[min] = 127;
        } else {
            for (i = min; i < max + 1; i++) {
                histo[i] = (i - min) * 255 / (max - min);
            }
        }
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                r = new Color(img.getRGB(x, y)).getGreen();
                dst.setRGB(x, y, new Color(histo[r], histo[r], histo[r]).getRGB());
            }
        }
        return dst;
    }

    public static BufferedImage filterBlackStuff(BufferedImage img) {
        int x, y;
        int r, ra, rb;
        final int w = img.getWidth();
        final int h = img.getHeight();
        final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        /* Remove vertical stuff */
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                dst.setRGB(x, y, img.getRGB(x, y));
                r = new Color(img.getRGB(x, y)).getRed();
                if (y > 0 && y < h - 1) {
                    ra = new Color(img.getRGB(x, y - 1)).getRed();
                    rb = new Color(img.getRGB(x, y + 1)).getRed();
                    if (r < ra && (r - ra) * (r - rb) > 5000) {
                        dst.setRGB(x, y, new Color(ra, ra, ra).getRGB());
                    }
                }
            }
        }

        /* Remove horizontal stuff */
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                r = new Color(img.getRGB(x, y)).getRed();
                if (x > 0 && x < w - 1) {
                    ra = new Color(img.getRGB(x - 1, y)).getRed();
                    rb = new Color(img.getRGB(x + 1, y)).getRed();

                    if (r < ra && (r - ra) * (r - rb) > 5000) {
                        dst.setRGB(x, y, new Color(ra, ra, ra).getRGB());
                    }
                }
            }
        }
        return dst;
    }
}