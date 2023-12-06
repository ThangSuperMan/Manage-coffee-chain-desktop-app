/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author thangphan
 */
public class Utils {
	public ImageIcon resizeImage(String imagePath, byte[] picture, JLabel imageLabel) {
		ImageIcon imageIcon = null;
		imageIcon = imagePath != null ? new ImageIcon(imagePath) : new ImageIcon(picture);
		Image image = imageIcon.getImage();
		Image image2 = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);

		return new ImageIcon(image2);
	}
}
