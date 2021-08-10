package db;

import core.Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbProcess {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/employees";
    private static final String DB_USER = "root";
    private static final String DB_PW = "0000";


    private final String selectNumber;

    // 생성자를 명시적으로 지정하면 기본생성자 DbProcess()는 호출될 수 없다. (따로 명시 해야 한다.)
    public DbProcess(String selectedNumber) {
        this.selectNumber = selectedNumber;
    }

    public void startProcessing() throws SQLException { //예외처리
        // Connection, PreparedStatement, ResultSet은 interface 객체이다.
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // selectedNumber에 따라 쿼리를 다르게 한다.
        switch (this.selectNumber) {
            case Menu.SELECT:
                showALLstudents(conn, pstmt, rs);
                break;
            case Menu.INSERT:
                break;
            case Menu.UPDATE:
                break;
            case Menu.DELETE:
                break;
            default:
                System.out.println("nothing to do");
                break;

        }
    }
    private List<Employee> showALLstudents(Connection conn, PreparedStatement pstmt, ResultSet rs) throws SQLException{
        List<Employee> sList = new ArrayList<>();
        pstmt = conn.prepareStatement("select * from Employees where emp_no between 10001 and 10020");
        rs = pstmt.executeQuery();
        while (rs.next()){
            System.out.println(
                    rs.getInt(1) + " | "
                    + rs.getString(2) + " | "
                    + rs.getString(3) + " | "
                    + rs.getString(4) + " | "
                    + rs.getString(5) + " | "
                    + rs.getDate(6));
            sList.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
        }
        return sList;
    }
}

