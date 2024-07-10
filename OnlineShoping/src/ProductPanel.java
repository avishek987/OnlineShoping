import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable productTable;
    private CartPanel cartPanel;
    private JButton addToCartButton;

    public ProductPanel(CartPanel cartPanel) {
        this.cartPanel = cartPanel;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Price"}, 0);
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new AddToCartActionListener());
        add(addToCartButton, BorderLayout.SOUTH);

        loadProducts();
    }

    public void loadProducts() {
        tableModel.setRowCount(0);
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, name, description, price FROM Product";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                tableModel.addRow(new Object[]{id, name, description, price});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class AddToCartActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ProductPanel.this, "Please select a product to add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            String productName = (String) tableModel.getValueAt(selectedRow, 1);
            String productDescription = (String) tableModel.getValueAt(selectedRow, 2);
            double productPrice = (double) tableModel.getValueAt(selectedRow, 3);

            Product product = new Product(productId, productName, productDescription, productPrice);
            cartPanel.addProductToCart(product);


            JOptionPane.showMessageDialog(ProductPanel.this, "Product added to cart successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
