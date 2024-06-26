import java.sql.*;

public class EmployeeManagementSystem {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/excelr";
    static final String USER = "root";
    static final String PASS = "admin";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            // Initialize the EmployeeTable
            createEmployeeTable(conn);
            
            // Perform operations
            addEmployee(conn, 101, "John Doe", "IT", 50000);
            displayAllEmployees(conn);
            updateEmployee(conn, 101, "John Doe", "IT", 60000);
            displayAllEmployees(conn);
            deleteEmployee(conn, 101);
            displayAllEmployees(conn);
            
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

    static void createEmployeeTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Employees " +
                     "(ID INT NOT NULL, " +
                     " Name VARCHAR(255), " +
                     " Department VARCHAR(255), " +
                     " Salary DOUBLE, " +
                     " PRIMARY KEY ( ID ))";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    static void addEmployee(Connection conn, int id, String name, String department, double salary) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Employees VALUES (?, ?, ?, ?)");
        pstmt.setInt(1, id);
        pstmt.setString(2, name);
        pstmt.setString(3, department);
        pstmt.setDouble(4, salary);
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("Employee added successfully.");
    }

    static void updateEmployee(Connection conn, int id, String name, String department, double salary) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("UPDATE Employees SET Name = ?, Department = ?, Salary = ? WHERE ID = ?");
        pstmt.setString(1, name);
        pstmt.setString(2, department);
        pstmt.setDouble(3, salary);
        pstmt.setInt(4, id);
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("Employee details updated successfully.");
    }
    

    static void deleteEmployee(Connection conn, int id) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Employees WHERE ID = ?");
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("Employee deleted successfully.");
    }

    static void displayAllEmployees(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Employees");
        System.out.println("Employee Details:");
        while (rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("Name");
            String department = rs.getString("Department");
            double salary = rs.getDouble("Salary");
            System.out.println("ID: " + id + ", Name: " + name + ", Department: " + department + ", Salary: " + salary);
        }
        rs.close();
        stmt.close();
    }
}
