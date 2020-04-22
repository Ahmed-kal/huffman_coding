package gui;

import javax.swing.*;
import java.awt.*;

public class ImageFrame implements Runnable {
    JFrame frame;
    ImageIcon imageIcon;
    public ImageFrame(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public void run() {
        frame = new JFrame("Huffman Tree");
        frame.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(imageIcon);
        frame.getContentPane().add(new JScrollPane(imageLabel));
        frame.pack();
        frame.setVisible(true);
    }
}
