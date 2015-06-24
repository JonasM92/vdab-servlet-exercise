package be.vdab;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/shop")
public class ShoppingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Tea shop</title>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h1>My order list</h1>");

        List<OrderItem> order = addOrderItem(req);
        displayOrders(writer, order);

        writer.println("</body>");
        writer.println("</html>");
    }

    private List<OrderItem> addOrderItem(HttpServletRequest req) {
        HttpSession session = req.getSession();
        List<OrderItem> order = (List<OrderItem>) session.getAttribute("order");
        if(order == null) {
            order = new ArrayList<>();
        }

        String product = req.getParameter("product");
        double price = Double.parseDouble(req.getParameter("price"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        order.add(new OrderItem(product, quantity, price));

        session.setAttribute("order", order);



        return order;
    }

    private void displayOrders(PrintWriter writer, List<OrderItem> orderList) {
        writer.println("<table border='1'>");

        writer.println("<tr>");
        writer.println("<td>Product</td>");
        writer.println("<td>Price</td>");
        writer.println("<td>Quantity</td>");
        writer.println("<td>Total</td>");
        writer.println("</tr>");

        for (OrderItem orderItem : orderList) {
            writer.println("<tr>");
            writer.println("<td>" + orderItem.getProduct() + "</td>");
            writer.println("<td>" + orderItem.getPrice() + "</td>");
            writer.println("<td>" + orderItem.getQuantity() + "</td>");
            writer.println("<td>" + orderItem.getTotal() + "</td>");
            writer.println("</tr>");
        }

        writer.println("</table>");

        double total = 0;
        for (OrderItem orderItem : orderList) {
            total += orderItem.getPrice();
        }
        writer.println("<div>Grand total: " + total + "</div>");
        writer.println("<a href='form.html'>More tea</a>");
    }

    public static class OrderItem {
        private String product;
        private int quantity;
        private double price;

        public OrderItem(String product, int quantity, double price) {
            this.product = product;
            this.quantity = quantity;
            this.price = price;
        }

        public String getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        public double getTotal() {
            return price * quantity;
        }
    }
}
