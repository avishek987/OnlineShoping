import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddProductPanel extends JPanel {
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JButton addButton;
    private ProductPanel productPanel;

    public AddProductPanel() {
        this.productPanel = productPanel;
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Description:"));
        descriptionField = new JTextField();
        add(descriptionField);

        add(new JLabel("Price:"));
        priceField = new JTextField();
        add(priceField);

        addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductToDatabase();
            }
        });
        add(addButton);
    }

    private void addProductToDatabase() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        double price;
        try {
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Product (name, description, price) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                productPanel.loadProducts();  // Refresh the product list
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        descriptionField.setText("");
        priceField.setText("");
    }
}



