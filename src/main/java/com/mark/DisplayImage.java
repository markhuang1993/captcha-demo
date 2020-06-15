package com.mark;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class DisplayImage {

    public static void main(String[] args) throws Exception {
//        BufferedImage img = ImageIO.read(new File("C:\\Users\\markh\\Downloads\\Two-examples-of-noisy-images-salt-and-pepper-noise-on-the-left-and-Gaussian-noise.png"));
        BufferedImage img = ImageIO.read(new File("C:\\Users\\markh\\Downloads\\zz.png"));

        img = ImageUtil.filterScale(img, 6);
        img = ImageUtil.filterSmooth(img);
        img = ImageUtil.filterBlackStuff(img);
//        img = ImageUtil.filterMedian(img);
//        img = ImageUtil.filterFillHoles(img);
        img = ImageUtil.filterDetectLines(img);




        printImg(img);
        Thread.sleep(9999);
    }

    public static void printImg(final BufferedImage img) {
        Thread thread = new Thread(() -> {
            ImageIcon icon = new ImageIcon(img);
            JFrame frame = new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(img.getWidth() + 80, img.getHeight() + 80);
            JLabel lbl = new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
        thread.setDaemon(true);
        thread.start();
    }
}