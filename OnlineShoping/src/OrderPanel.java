import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Object;
public class OrderPanel<Timestamp> extends JPanel {
    private DefaultTableModel tableModel;
    private JTable orderTable;

    public OrderPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Product ID", "Name", "Description", "Price", "Order Date"}, 0);
        orderTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        loadOrders();
    }

    public void loadOrders() {
        tableModel.setRowCount(0);
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, product_id, product_name, product_description, product_price, order_date FROM Orders";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                String productDescription = resultSet.getString("product_description");
                double productPrice = resultSet.getDouble("product_price");
                Timestamp orderDate = (Timestamp) resultSet.getTimestamp("order_date");

                tableModel.addRow(new Object[]{orderId, productId, productName, productDescription, productPrice, orderDate});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
