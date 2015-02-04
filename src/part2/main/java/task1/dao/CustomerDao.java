package part2.main.java.task1.dao;

import org.xml.sax.SAXException;
import part2.main.java.task1.model.Customer;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface CustomerDao {

    List<Customer> getCustomers() throws ParserConfigurationException, SAXException, IOException;

    BigDecimal getSumAllOrdersPrice();

    Long getCustomerIdWithMaxSumOrdersPrice();

    BigDecimal getBiggestOrderSum();

    BigDecimal getSmallestOrderSum();

    Long getOrdersAmount();

    BigDecimal getAverageOrderSumPrice();

    Set<Customer> getCustomersWithOrdersSumMoreThanN(BigDecimal n);

}
