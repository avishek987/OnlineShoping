import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("E-Shopping");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        ImageIcon icon = new ImageIcon("Logo.png");
        frame.setIconImage(icon.getImage());


        CartPanel cartPanel = new CartPanel();
        ProductPanel productPanel = new ProductPanel(cartPanel);
        OrderPanel orderPanel = new OrderPanel();
        AddProductPanel addProductPanel = new AddProductPanel();


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Products", productPanel);
        tabbedPane.addTab("Cart", cartPanel);
        tabbedPane.addTab("Orders", orderPanel);
        tabbedPane.addTab("Add Product", addProductPanel);


        frame.add(tabbedPane, BorderLayout.CENTER);


        frame.setVisible(true);
    }
}

