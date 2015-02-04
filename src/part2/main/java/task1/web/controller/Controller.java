package part2.main.java.task1.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import part2.main.java.task1.dao.CustomerDao;
import part2.main.java.task1.model.Customer;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;


@org.springframework.stereotype.Controller
public class Controller {

    CustomerDao customerDao;

    @Autowired
    public Controller(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @ModelAttribute("customers")
    public List<Customer> getCustomerList() throws ParserConfigurationException, SAXException, IOException {
        return customerDao.getCustomers();
    }

    @RequestMapping(value = "/webFormPage", method = RequestMethod.GET)
    public String initIndexPage(Map<String,Object> model){
        BigDecimal x1 = customerDao.getSumAllOrdersPrice();
        BigDecimal x2 = customerDao.getBiggestOrderSum();
        BigDecimal x3 = customerDao.getSmallestOrderSum();
        Long x4 = customerDao.getOrdersAmount();
        BigDecimal x5 = customerDao.getAverageOrderSumPrice();
        model.put("x1",x1);
        model.put("x2",x2);
        model.put("x3",x3);
        model.put("x4",x4);
        model.put("x5",x5);
        return "webFormPage";
    }

    @RequestMapping(value = "/webFormPage", method = RequestMethod.POST)
    public String getCustomersWithSumMoreN(@RequestParam BigDecimal n, Map<String,Object> model){
        Set<Customer> list = customerDao.getCustomersWithOrdersSumMoreThanN(n);
        model.put("customerList", list);
        return "webFormPage";
    }
}
