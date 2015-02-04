package part2.main.java.task1.dao;

import org.xml.sax.SAXException;
import part2.main.java.task1.model.Customer;
import part2.main.java.task1.model.Order;
import part2.main.java.task1.model.Position;
import part2.main.java.task1.util.XMLParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerDaoImpl implements CustomerDao{

    private XMLParser parser = new XMLParser();
    private List<Customer> customers;

    public CustomerDaoImpl() throws IOException, SAXException, ParserConfigurationException {
        this.customers = getCustomers();
    }

    @Override
    public BigDecimal getSumAllOrdersPrice() {
        BigDecimal sum = BigDecimal.valueOf(0L);
        for (Customer customer : customers) {
            for (Order order : customer.orders) {
                for (Position position : order.positions) {
                    sum = sum.add(position.getPrice().multiply(BigDecimal.valueOf(position.count)));
                }
            }
        }
        return sum;
    }

    @Override
    public Long getCustomerIdWithMaxSumOrdersPrice() {
        Long custId = 0L;
        BigDecimal maxSum = new BigDecimal("0");
        for (Customer customer : customers) {
            for (Order order : customer.orders) {
                for (Position position : order.positions) {
                    BigDecimal sum = position.getPrice().multiply(BigDecimal.valueOf(position.getCount()));
                    if(sum.compareTo(maxSum) > 0){
                        maxSum = sum;
                        custId = customer.id;
                    }
                }
            }
        }
        return custId;
    }

    @Override
    public BigDecimal getBiggestOrderSum() {
        Long biggestAmount = 0L;
        Order biggestOrder = null;
        for (Customer customer : customers) {
            for (Order order : customer.orders) {
                Long amount = getCounts(order);
                if( amount > biggestAmount){
                    biggestAmount = amount;
                    biggestOrder = order;
                }
            }
        }
        return getOrderSum(biggestOrder);
    }

    @Override
    public BigDecimal getSmallestOrderSum() {
        Long smallestAmount = Long.MAX_VALUE;
        Order smallestOrder = null;
        for (Customer customer : customers) {
            for (Order order : customer.orders) {
                Long amount = getCounts(order);
                if( amount < smallestAmount){
                    smallestAmount = amount;
                    smallestOrder = order;
                }
            }
        }
        return getOrderSum(smallestOrder);
    }

    @Override
    public Long getOrdersAmount() {
        Long ordersAmount = 0L;
        for (Customer customer : customers) {
            ordersAmount += customer.orders.size();
        }
        return ordersAmount;
    }

    @Override
    public Set<Customer> getCustomersWithOrdersSumMoreThanN(BigDecimal n) {
        Set<Customer> customersList= new HashSet<>();
        for (Customer customer : customers) {
            for (Order order : customer.orders) {
                BigDecimal sum = getOrderSum(order);
                if(sum.compareTo(n) > 0) {
                    customersList.add(customer);
                }
            }
        }
        return customersList;
    }

    private Long getCounts(Order order){
        Long amount = 0L;
        for (Position position : order.positions) {
            amount = amount + position.count;
        }
        return amount;
    }

    private BigDecimal getOrderSum(Order order){
        BigDecimal sum = new BigDecimal("0");
        for (Position position: order.positions) {
            sum = sum.add(position.price.multiply(BigDecimal.valueOf(position.count)));
        }
        return sum;
    }

    @Override
    public List<Customer> getCustomers() throws ParserConfigurationException, SAXException, IOException {
        return parser.parseXML().getCustomers();
    }

    @Override
    public BigDecimal getAverageOrderSumPrice() {
        return BigDecimal.valueOf(getSumAllOrdersPrice().longValue()/getOrdersAmount());
    }
}
