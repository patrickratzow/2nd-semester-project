package gui.components.customer;

import controller.CustomerController;
import entity.Customer;
import gui.components.core.PanelManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FindOrCreateCustomer extends JPanel {
	private JTextField searchTextField;
	private CustomerController customerController;
	private JComponent resultComponent;
	private PanelManager panelManager;
	private String previousId;

	public FindOrCreateCustomer(PanelManager panelManager) {
		customerController = new CustomerController();
		previousId = panelManager.getCurrentId();

		JLabel lblNewLabel = new JLabel("Kunde");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		setLayout(new MigLayout("", "[373px,grow]", "[22px][][19px][grow]"));
		add(lblNewLabel, "cell 0 0,growx,aligny top");

		searchTextField = new JTextField();
		searchTextField.setColumns(10);
		searchTextField.setForeground(Color.GRAY);
		String placeholderText = "Telefonnummer";
		searchTextField.setText(placeholderText);
		searchTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchTextField.getText().equals(placeholderText)) {
					searchTextField.setText("");
					searchTextField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchTextField.getText().isEmpty()) {
					searchTextField.setForeground(Color.GRAY);
					searchTextField.setText(placeholderText);
				}
			}
		});
		add(searchTextField, "flowx,cell 0 1,alignx left,aligny top");

		JButton btnSearch = new JButton("Anmod");
		btnSearch.addActionListener(e -> customerController.getSearch(searchTextField.getText()));
		add(btnSearch, "cell 0 1");

		JButton btnCreate = new JButton("Opret kunde");
		btnCreate.addActionListener(l -> panelManager.setActive("create_customer", 
				() -> new CreateCustomer(panelManager)));
		add(btnCreate, "cell 0 1");

		customerController.addFindListener(customers -> {
			if (resultComponent != null) {
				remove(resultComponent);
			}
			if (!customers.isEmpty()) {
				createCustomerDisplay(customers.get(0));
			} else {
				createNoResultDisplay();
			}
			revalidate();
			repaint();
		});
	}
	
	private void createCustomerDisplay(Customer customer) {
		resultComponent = new JPanel();
		add(resultComponent, "cell 0 3,grow");
		resultComponent.setLayout(new BorderLayout());
		CustomerInformationBox customerInformationBox = new CustomerInformationBox(customer);
		resultComponent.add(customerInformationBox, BorderLayout.WEST);
	}
	
	private void createNoResultDisplay() {
		resultComponent = new JLabel(searchTextField.getText() + " does not exist");
		resultComponent.setFont(new Font("Tahoma", Font.PLAIN, 18));
		resultComponent.setForeground(Color.RED);
		add(resultComponent, "cell 0 2");
	}

}