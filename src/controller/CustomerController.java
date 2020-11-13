package controller;

import exception.DataAccessException;
import exception.DataWriteException;
import model.Customer;
import persistance.CustomerDao;
import persistance.mssql.CustomerDaoMsSql;

import java.util.List;

public class CustomerController {
    CustomerDao customerDao = new CustomerDaoMsSql();

    public List<Customer> findAll() throws DataAccessException {
        return customerDao.findAll();
    }

    public Customer findById(int id) throws DataAccessException {
        return customerDao.findById(id);
    }

    public Customer create(Customer customer) throws DataWriteException {
    	return customerDao.create(customer.getFirstName(), customer.getLastName(),
    			customer.getEmail(), customer.getPhoneNo());
    }
    
    public void update(Customer customer) throws DataWriteException, DataAccessException {
    	customerDao.update(customer.getId(), customer.getFirstName(),
    			customer.getLastName(), customer.getEmail(), 
    			customer.getPhoneNo());
    }
    
    // TODO: Make changes
    public void findByPhoneNo(String phoneNo) throws DataAccessException {
    	customerDao.findByPhoneNo(phoneNo);
    }
}
