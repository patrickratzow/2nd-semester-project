package gui;

import gui.components.SpecificationRow;
import gui.components.TitleBar;

import javax.swing.*;

import controller.SpecificationController;
import entity.Requirement;
import entity.Specification;

import java.awt.*;
import java.util.List;

public class SpecificationTab extends JPanel {
	
	private SpecificationController specificationController;

	/**
	 * Create the panel.
	 */
	
	public SpecificationTab(Specification specification) {
		specificationController = new SpecificationController(specification);
		List<Requirement> requirements = specificationController.getRequirements();
		
		setLayout(new BorderLayout(0, 0));
		
		TitleBar title = new TitleBar();
		title.setTitle("Specification");
		title.setButtonName("Gaa Tilbage");
		add(title, BorderLayout.NORTH);
		
		JPanel buttomBar = new JPanel();
		add(buttomBar, BorderLayout.SOUTH);
		buttomBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		
		JButton save = new JButton("Gem krav");
		save.setHorizontalAlignment(SwingConstants.RIGHT);
		buttomBar.add(save);
		save.addActionListener(e -> System.out.println("Krav er gemt"));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		panel.add(createSpecificationRow("Navn"));
		panel.add(createSpecificationRow("Antal"));
		
		for (Requirement requirement : requirements) {
			panel.add(createRequirementRow(requirement));
		}
	}
	
	//send Jcomponent;
	private SpecificationRow createRequirementRow(Requirement requirement) {
		SpecificationRow rows = new SpecificationRow();
		rows.setTitleName(requirement.getName());
		
		return rows;
	}
	
	private SpecificationRow createSpecificationRow(String displayValue) {
		SpecificationRow rows = new SpecificationRow();
		rows.setTitleName(displayValue);
		
		return rows;
	}
	
}
