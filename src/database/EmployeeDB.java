package database;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDB implements IEmployeeDB {
    private static final String FIND_BY_USERNAME_Q = "SELECT * FROM GetEmployees WHERE employeeUsername = ?";
    private PreparedStatement findByUsernamePS;
    private static final String FIND_ALL_Q = "SELECT * FROM GetEmployees";
    private PreparedStatement findAllPS;
    private static final String INSERT_Q = "{CALL InsertEmployee(?, ?, ?, ?, ?, ?, ?, ?)}";
    private CallableStatement insertPC;
    private static final String UPDATE_Q = "{CALL UpdateEmployee(?, ?, ?, ?, ?, ?, ?, ?)}";
    private CallableStatement updatePC;

    public EmployeeDB() {
        init();
    }

    private void init() {
        DBConnection con = DBConnection.getInstance();

        try {
            findByUsernamePS = con.prepareStatement(FIND_BY_USERNAME_Q);
            findAllPS = con.prepareStatement(FIND_ALL_Q);
            insertPC = con.prepareCall(INSERT_Q);
            updatePC = con.prepareCall(UPDATE_Q);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Employee buildObject(ResultSet rs) {
        Employee employee = new Employee();

        try {
            employee.setId(rs.getInt("personId"));
            employee.setFirstName(rs.getString("personFirstName"));
            employee.setLastName(rs.getString("personLastName"));
            employee.setEmail(rs.getString("personEmail"));
            employee.setPhoneNo(rs.getString("personPhoneNo"));
            employee.setUsername(rs.getString("employeeUsername"));
            byte[] hash = rs.getBytes("employeePassword");
            byte[] salt = rs.getBytes("employeeSalt");
            employee.setPassword(new Password(hash, salt));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
    }


    private List<Employee> buildObjects(ResultSet rs) {
        List<Employee> employees = new ArrayList<>();

        try {
            while (rs.next()) {
                Employee employee = buildObject(rs);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    @Override
    public Employee findByUsername(String username) throws DataAccessException {
        Employee employee = null;

        try {
            findByUsernamePS.setString(1, username);
            ResultSet rs = this.findByUsernamePS.executeQuery();

            if (rs.next()) {
                employee = buildObject(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to find an employee");
        }

        if (employee == null) {
            throw new DataAccessException("Unable to find an employee");
        }

        return employee;
    }

    @Override
    public List<Employee> findAll() throws DataAccessException {
        List<Employee> employees;

        try {
            ResultSet rs = this.findAllPS.executeQuery();
            employees = buildObjects(rs);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to find any employees");
        }

        if (employees.size() == 0) {
            throw new DataAccessException("Unable to find any employees");
        }

        return employees;
    }

    @Override
    public Employee create(String firstName, String lastName,
                       String email, String phoneNo, String username, byte[] password, byte[] salt) throws DataWriteException {
        Employee employee = new Employee();

        try {
            insertPC.setString(1, firstName);
            insertPC.setString(2, lastName);
            insertPC.setString(3, email);
            insertPC.setString(4, phoneNo);
            insertPC.setString(5, username);
            insertPC.setBytes(6, password);
            insertPC.setBytes(7, salt);
            insertPC.registerOutParameter(8, Types.INTEGER);
            insertPC.execute();

            employee.setId(insertPC.getInt(8));
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setEmail(email);
            employee.setPhoneNo(phoneNo);
            employee.setUsername(username);
            employee.setPassword(new Password(password, salt));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataWriteException("Unable to create employee");
        }

        return employee;
    }

    @Override
    public void update(int id, String firstName, String lastName,
                       String email, String phoneNo, String username, byte[] password, byte[] salt) throws DataWriteException, DataAccessException {
        try {
            updatePC.setInt(1, id);
            updatePC.setString(2, firstName);
            updatePC.setString(3, lastName);
            updatePC.setString(4, email);
            updatePC.setString(5, phoneNo);
            updatePC.setString(6, username);
            updatePC.setBytes(7, password);
            updatePC.setBytes(8, salt);
            int affected = updatePC.executeUpdate();
            if (affected == 0) {
                throw new DataAccessException("Unable to find any employee to update");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataWriteException("Unable to update employee");
        }
    }
}