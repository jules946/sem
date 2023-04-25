package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        // Create new Application
        App a = new App();

        // Connect to database
        if(args.length < 1){
            a.connect("localhost:33060", 30000);
        }else{
            a.connect(args[0], Integer.parseInt(args[1]));
        }


        // salaries by role
            //test a role
            //String role = "Engineer";

            // Extract employee salary information
            //ArrayList<Employee> employees = a.getRoleSalaries(role);

            //print out all salaries
            //a.printSalaries(employees);

        //salaries by department


            //get department
            Department department1 = a.getDepartment("Sales");

            //print department details
            a.displayDepartment(department1);

            // Extract employee salary info
            ArrayList<Employee> employees1 = a.getSalariesByDepartment(department1);

            //print out all salaries
            a.printSalaries(employees1);

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " +                                  Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Check whether the database connection is established or not.
     *
     * @return true if the connection is established, false otherwise
     */
    public boolean isConnected() {
        return con != null;
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            } finally {
                // Set connection object to null
                con = null;
            }
        }
    }


    public Employee getEmployee(int ID) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, titles.title, salaries.salary, departments.dept_name, dept_manager.emp_no, departments.dept_no "
                            + "FROM employees "
                            + "JOIN titles ON (employees.emp_no = titles.emp_no) "
                            + "JOIN salaries ON (employees.emp_no = salaries.emp_no) "
                            + "JOIN dept_emp ON (dept_emp.emp_no = employees.emp_no) "
                            + "JOIN departments ON (departments.dept_no = dept_emp.dept_no) "
                            + "LEFT JOIN dept_manager ON (departments.dept_no = dept_manager.dept_no AND dept_manager.to_date = '9999-01-01') "
                            + "WHERE employees.emp_no = " + ID;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.title = rset.getString("titles.title");
                emp.salary = rset.getInt("salaries.salary");
                emp.manager = rset.getInt("dept_manager.emp_no");
                emp.dept = rset.getString("departments.dept_no");

                return emp;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }



    public void displayEmployee(Employee emp) {
        if (emp != null) {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + "Title: " + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + "Department: " + emp.dept + "\n"
                            + "Manager ID: " + emp.manager + "\n");
        }
    }

    /**
     * Gets all the current employees and salaries.
     *
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    public ArrayList<Employee> getRoleSalaries(String title) {
        String sqlTitle = "'" + title + "'";
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees "
                            + "JOIN titles ON (employees.emp_no = titles.emp_no)"
                            + "JOIN salaries ON (employees.emp_no = salaries.emp_no)"
                            + "WHERE salaries.to_date = '9999-01-01' AND titles.to_date = '9999-01-01' AND titles.title = " + sqlTitle
                            + " ORDER BY employees.emp_no ASC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Gets a department based on department name
     */
        public Department getDepartment(String dept_name){

            String Sqldept_name = "'" + dept_name + "'";
            try {
                // Create an SQL statement
                Statement stmt = con.createStatement();
                // Create string for SQL statement
                String strSelect =
                        "SELECT departments.dept_no, departments.dept_name, dept_manager.emp_no "
                                + "FROM departments "
                                + "JOIN dept_manager ON (departments.dept_no = dept_manager.dept_no) "
                                + "WHERE departments.dept_name = " + Sqldept_name;

                // Execute SQL statement
                ResultSet rset = stmt.executeQuery(strSelect);

                /// Return department if valid.
                if (rset.next()) {
                    Department dep = new Department();
                    dep.dept_no = rset.getString("departments.dept_no");
                    dep.dept_name = rset.getString("departments.dept_name");
                    dep.manager = rset.getInt("dept_manager.emp_no");

                    return dep;

                }
                return null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("Failed to get department details");
                return null;
            }

        }

    public void displayDepartment(Department dep) {
        if (dep != null) {
            System.out.println(
                    "Department Name: " + dep.dept_name + "\n"
                            + "Department Number: " + dep.dept_no + "\n"
                            + "Manager ID: " + dep.manager + "\n");
        }
    }

    /**
     * gets salaries of all employees based on a department
     */
    public ArrayList<Employee> getSalariesByDepartment(Department dept){

        String sqlDepName = "'" + dept.dept_no + "'";
        try {

            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
            + "FROM employees, salaries, dept_emp, departments "
                            + "WHERE employees.emp_no = salaries.emp_no "
                            + "AND employees.emp_no = dept_emp.emp_no "
                            + "AND dept_emp.dept_no = departments.dept_no "
                            + "AND salaries.to_date = '9999-01-01' "
                            + "AND departments.dept_no = " + sqlDepName
                            + " ORDER BY employees.emp_no ASC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }

    }

    /**
     * Prints a list of employees.
     *
     * @param employees The list of employees to print.
     */
    public void printSalaries(ArrayList<Employee> employees) {

        //check employees is not nu;;
        if (employees == null){
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));

        // Loop over all employees in the list
        for (Employee emp : employees) {
            if(emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

}

