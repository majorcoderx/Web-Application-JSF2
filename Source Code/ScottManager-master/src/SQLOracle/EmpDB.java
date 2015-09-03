package SQLOracle;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import Models.Users;

public class EmpDB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sql;
	
	public List<Users> getListDirector(int empno){
		List<Users> luser = new LinkedList<Users>();
		sql = "select level, deptno from emp where empno = "
				+empno+" connect by prior empno = mgr";
		int level = 0, deptno = 0;
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				level = ConnToDB.rs.getInt(1);
				deptno = ConnToDB.rs.getInt(2);
			}
			sql = "select empno, ename, deptno "
					+ "from emp where level <= "+ level +" or deptno != "+ deptno
					+ " connect by prior empno = mgr "
					+ "start with mgr = 0";
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				Users user = new Users();
				user.setEmpno(ConnToDB.rs.getInt(1));
				user.setEname(ConnToDB.rs.getString(2));
				user.setDeptno(ConnToDB.rs.getInt(3));
				luser.add(user);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return luser;
	}
}
