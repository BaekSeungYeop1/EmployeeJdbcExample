package db;

import Input.InputUtil;
import core.Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
                //직원 정보 조회(emp_no가 10001~10020)
                showALLstudents(conn, pstmt, rs);
                break;
            case Menu.INSERT:
                Employee s = Employee.buildStudent();
                // 새로운 map_no를 db에 insert 하기 위해 max emp_no+1을 가져온다
                pstmt = conn.prepareStatement("select max(emp_no) + 1 from Employees");
                rs = pstmt.executeQuery();
                if (rs.next()){
                    int maxEmp_no = rs.getInt(1);
                    System.out.println("maxEmp_no = " + maxEmp_no);
                    pstmt = conn.prepareStatement("insert into Employees values (?,?,?,?,?,?)");
                    // "?"의 순서에 (1부터)에 따라 값을 셋팅
                    pstmt.setInt(1, maxEmp_no);
                    pstmt.setString(2, s.getBirth_date());
                    pstmt.setString(3,s.getFirst_name());
                    pstmt.setString(4, s.getLast_name());
                    pstmt.setString(5,s.getGender());
                    pstmt.setString(6,s.getHire_date());

                    System.out.println("INSERT 완료");

                    int updateRows = pstmt.executeUpdate();
                    System.out.println("updateedRows: " + updateRows);
                }else {
                    throw new SQLException("Can't execute query select max(id) + 1 from student");
                }
                break;
            case Menu.UPDATE:
                //직원 정보 수정
                System.out.println("직원 정보의 수정");
                //콘솔 출력 : (사용자에게 "1"을 입력받을 시 Employee 테이블에서 emp_no 10001~10020인 직원을 조회
                List<Employee> employees = showALLstudents(conn, pstmt, rs);
                //콘솔 출력 : 수정할 직원의 번호(emp_no)을 입력하세요
                System.out.println("수정할 직원의 번호를 입력하세요");
                int sIDToUpdate = InputUtil.getIdFromEmployeeList(employees);
                System.out.println("수정할 학생 번호 : " + sIDToUpdate );
                // 출생년도,이름,성,성별,입사년도를 입력받는다
                Employee updateStd = Employee.buildStudent();
                // 업데이트 쿼리 수행
                pstmt = conn.prepareStatement("UPDATE Employees SET birth_date=?,first_name=?,last_name=?,gender=?,hire_date=? where emp_no=?");
                pstmt.setString(1, updateStd.getBirth_date());
                pstmt.setString(2, updateStd.getFirst_name());
                pstmt.setString(3, updateStd.getLast_name());
                pstmt.setString(4, updateStd.getGender());
                pstmt.setString(5, updateStd.getHire_date());
                pstmt.setInt(6,sIDToUpdate);
                pstmt.executeUpdate();
                System.out.println("업데이트 완료.");
                break;
            case Menu.DELETE:
                // 콘솔 출력 : 학생 정보의 삭제
                System.out.println("학생 정보의 삭제");
                // 전체 학생의 목록 출력
                List<Employee> EmployeeList = showALLstudents(conn, pstmt, rs);
                // 콘솔 출력
                System.out.println("삭제할 학생의 번호를 입력 하세요");
                int sIdToDelete = InputUtil.getIdFromEmployeeList(EmployeeList);
                // 삭제 쿼리 수행
                pstmt = conn.prepareStatement("DELETE FROM employees where emp_no=?");
                pstmt.setInt(1, sIdToDelete);
                pstmt.executeUpdate();
                System.out.println("직원 emp_no " + sIdToDelete + "삭제 완료.");
                break;
            case Menu.SELECT_GENDER:
                Scanner sc = new Scanner(System.in);
                // 콘솔 출력 : 검색하려는 성별을 입력하세요
                System.out.println("검색하려는 성별을 입력하세요 (F or M)");
                //
                String gender = sc.nextLine();
                if (Objects.equals(gender, "F")) {
                    List<Employee> fList = new ArrayList<>();
                    pstmt = conn.prepareStatement("select * from Employees where gender = 'F' ");
                    rs = pstmt.executeQuery();
                    while (rs.next()){
                        System.out.println(
                                rs.getInt(1) + " | "
                                        + rs.getString(2) + " | "
                                        + rs.getString(3) + " | "
                                        + rs.getString(4) + " | "
                                        + rs.getString(5) + " | "
                                        + rs.getDate(6));
                        fList.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
                    }

                }
                else if (Objects.equals(gender, "M"))  {
                    List<Employee> mList = new ArrayList<>();
                    pstmt = conn.prepareStatement("select * from Employees where gender = 'M' ");
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        System.out.println(
                                rs.getInt(1) + " | "
                                        + rs.getString(2) + " | "
                                        + rs.getString(3) + " | "
                                        + rs.getString(4) + " | "
                                        + rs.getString(5) + " | "
                                        + rs.getDate(6));
                        mList.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
                    }
                }
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

