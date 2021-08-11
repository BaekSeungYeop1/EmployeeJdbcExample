package db;

import Input.InputUtil;

import java.security.PublicKey;
import java.util.Date;

public class Employee {
    private int emp_no;
    private String birth_date;
    private String first_name;
    private String last_name;
    private String gender;
    private String hire_date;

    public Employee(int emp_no, String birth_date, String first_name, String last_name, String gender, String hire_date){
        this.emp_no = emp_no;
        this.birth_date = birth_date;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.hire_date = hire_date;
    }

    public Employee() {

    }

    public int getEmp_no() {
        return this.emp_no;
    }

    public String getBirth_date() {
        return this.birth_date;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public String getGender() {
        return this.gender;
    }

    public String getHire_date() {
        return this.hire_date;
    }

    public static Employee buildStudent() {
        Employee s = new Employee();
        System.out.println("생일은?");
        s.birth_date = InputUtil.getStringFromConsole("no bithday");
        System.out.println("이름은?");
        s.first_name = InputUtil.getStringFromConsole("no first name");
        System.out.println("성은?");
        s.last_name = InputUtil.getStringFromConsole("no last name");
        System.out.println("성별은?");
        s.gender = InputUtil.getStringFromConsole("no gender");
        System.out.println("입사년도는?");
        s.hire_date = InputUtil.getStringFromConsole("no hire_date");
        return s;
    }


}
