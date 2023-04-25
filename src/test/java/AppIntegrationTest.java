import com.napier.sem.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
public class AppIntegrationTest {
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 30000);
    }

    /**
     * Test case to verify that the 'getEmployee()' method of the 'App' class retrieves
     * an employee record from the MySQL database correctly.
     *
     * This test calls the 'getEmployee()' method of the 'App' class with the employee ID
     * 255530 and then checks whether the employee's first name and last name match the
     * expected values "Ronghao" and "Garigliano", respectively. If the employee record is
     * not retrieved or if the first name and last name values do not match the expected
     * values, the test will fail.
     */
    @Test
    void testGetEmployee()
    {
        // Retrieve employee record with emp_no 255530
        Employee emp = app.getEmployee(255530);

        // Verify that the retrieved employee record matches the expected values
        assertEquals(emp.emp_no, 255530);
        assertEquals(emp.first_name, "Ronghao");
        assertEquals(emp.last_name, "Garigliano");
    }

    /**
     * Test case to verify that the application can connect to the database successfully.
     * This test calls the 'connect()' method of the 'App' class with the appropriate
     * connection parameters and then checks if the connection is established by calling
     * the 'isConnected()' method. The test will fail if the connection is not established
     * or if the 'isConnected()' method returns 'false'.
     */
    @Test
    void testConnect() {
        // Attempt to connect to the database
        app.connect("localhost:33060", 30000);

        // Verify that the connection is established
        assertTrue(app.isConnected(), "Failed to connect to database");
    }

    /**
     * Test case to verify that the application can disconnect from the database successfully.
     * This test calls the 'disconnect()' method of the 'App' class to close the database connection
     * and then checks if the connection is closed by calling the 'isConnected()' method. The test
     * will fail if the connection is still open or if the 'isConnected()' method returns 'true'.
     */
    @Test
    void testDisconnect() {
        // Disconnect from the database
        app.disconnect();

        // Verify that the connection is closed
        assertFalse(app.isConnected(), "Failed to disconnect from database");
    }


}
