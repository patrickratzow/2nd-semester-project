package gui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Row extends JPanel {
	/**
	 * Create the panel.
	 */
	public Row() {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		JPanel rightContainer = new JPanel();
		rightContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(rightContainer, BorderLayout.EAST);
		rightContainer.setLayout(new BorderLayout(0, 0));
		
		JButton mainButton = new JButton("New button");
		mainButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		rightContainer.add(mainButton);
		
		JPanel leftContainer = new JPanel();
		add(leftContainer, BorderLayout.WEST);
		leftContainer.setLayout(new BorderLayout(0, 0));
		
		JLabel title = new JLabel("New label");
		title.setBorder(new EmptyBorder(5, 5, 5, 5));
		title.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		leftContainer.add(title);

	}

}
