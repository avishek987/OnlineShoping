import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CartPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable cartTable;
    private List<Product> cartProducts;
    private JButton orderButton;

    public CartPanel() {
        setLayout(new BorderLayout());

        cartProducts = new ArrayList<>();
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Price"}, 0);
        cartTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        orderButton = new JButton("Place Order");
        orderButton.addActionListener(new OrderActionListener());
        add(orderButton, BorderLayout.SOUTH);
    }

    public void addProductToCart(Product product) {
        cartProducts.add(product);
        tableModel.addRow(new Object[]{product.getId(), product.getName(), product.getDescription(), product.getPrice()});
    }

    private class OrderActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            placeOrder();
        }

        private void placeOrder() {
            if (cartProducts.isEmpty()) {
                JOptionPane.showMessageDialog(CartPanel.this, "Cart is empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = Database.getConnection()) {
                String sql = "INSERT INTO Orders (product_id, product_name, product_description, product_price, order_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(sql);

                for (Product product : cartProducts) {
                    statement.setInt(1, product.getId());
                    statement.setString(2, product.getName());
                    statement.setString(3, product.getDescription());
                    statement.setDouble(4, product.getPrice());
                    statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    statement.addBatch();
                }

                int[] rowsInserted = statement.executeBatch();
                if (rowsInserted.length > 0) {
                    JOptionPane.showMessageDialog(CartPanel.this, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    cartProducts.clear();
                    tableModel.setRowCount(0);  // Clear the cart table
                } else {
                    JOptionPane.showMessageDialog(CartPanel.this, "Failed to place order.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(CartPanel.this, "Error placing order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}






