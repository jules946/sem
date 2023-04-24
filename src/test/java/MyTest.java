import com.napier.sem.App;
import com.napier.sem.Employee;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class MyTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    /**
     * Print Salaries Tests
     */

    @Test
    void printSalariesTestNull()
    {
        app.printSalaries(null);
    }

    @Test
    void printSalariesTestEmpty()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        app.printSalaries(employess);
    }
    @Test
    void printSalariesTestContainsNull()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        employess.add(null);
        app.printSalaries(employess);
    }
    @Test
    void printSalariesAllNonNull()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.printSalaries(employees);
    }

    /**
     * Display Employee Tests
     */

    @Test
    void displayEmployeeNull(){
        app.displayEmployee(null);
    }


    @Test
    void displayEmployeeWithNullProperties(){
        Employee emp = new Employee();
        emp.emp_no = null;
        emp.first_name = "John";
        emp.last_name = null;
        emp.title = null;
        emp.salary = null;
        emp.dept = null;
        emp.manager = null;
        app.displayEmployee(emp);
    }

    @Test
    void displayValidEmployee(){
        Employee emp = new Employee();
        emp.emp_no = 12345;
        emp.first_name = "John";
        emp.last_name = "Doe";
        emp.title = "Software Engineer";
        emp.salary = 100000;
        emp.dept = "Engineering";
        emp.manager = 9876;
        app.displayEmployee(emp);
    }

}