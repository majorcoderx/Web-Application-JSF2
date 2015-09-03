package SQLOracle;

import java.sql.SQLException;

import Models.Users;
import Service.StaticValue;

public class LoginDB {
	private String sql;
	
	public void getUser(int empno, String password){
		StaticValue.userLog = new Users();
		try{
			sql = "select empno, password, job, deptno from emp where empno = "
					+empno+" and password = "+password;
			ConnToDB.CreateConn();
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				StaticValue.userLog.setEmpno(ConnToDB.rs.getInt(1));
				StaticValue.userLog.setPassword(ConnToDB.rs.getString(2));
				StaticValue.userLog.setJob(ConnToDB.rs.getString(3));
				StaticValue.userLog.setDeptno(ConnToDB.rs.getInt(4));
				StaticValue.isLogged = true;
			}
			ConnToDB.rs.close();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
}
