package SQLOracle;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import Models.*;
import Service.StaticValue;

public class ProfileDB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sql;
	PreparedStatement preparedStatement = null;

	public boolean saveToDB(Users user){		
		boolean check = false;
		java.sql.Date sqlDate = new java.sql.Date(user.getHiredate().getTime());
		sql = "UPDATE emp SET ename = ?, mgr = ?, hiredate = ?, deptno = ? WHERE empno = ?";
		try{
			preparedStatement = ConnToDB.conn.prepareStatement(sql);
			preparedStatement.setString(1, user.getEname());
			preparedStatement.setInt(2, user.getMgr());
			preparedStatement.setDate(3, sqlDate);
			preparedStatement.setInt(4, user.getDeptno());
			preparedStatement.setInt(5, user.getEmpno());
			preparedStatement.executeUpdate();
			check = true;
		}catch(SQLException ex){
			ex.printStackTrace();
			check = false;
		}
		return check;
	}
	
	public String getManagerNameFromDname(String dname){
		String mgrName = "";
		sql = "select e.ename from emp e, dept d where d.dname  = '" 
		+ dname +"' and e.job = 'MANAGER' and e.deptno = d.deptno";
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				mgrName = ConnToDB.rs.getString(1);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return mgrName;
	}
	
	public Department getDeptFromName(String dname){
		sql =  "select deptno ,loc from dept where dname = '"+dname+"'";
		Department dept = new Department();
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				dept.setDeptno(ConnToDB.rs.getInt(1));
				dept.setLoc(ConnToDB.rs.getString(2));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return dept;
	}
	
	public List<Assignment> getProjectInfo(int empno){
		List<Assignment> aList = new ArrayList<Assignment>();
		sql = "select p.projid, p.p_desc, a.a_start_date,a.a_end_date,a.bill_amount,a.assign_type,a.hours"
				+ " from project p, assignments a "+ 
				"where a.empno = "+ empno + " and a.projid = p.projid";
		try {
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while (ConnToDB.rs.next()) {
				Assignment a = new Assignment();
				Project proj = new Project();
				proj.setProjId(ConnToDB.rs.getInt(1));
				proj.setDescrible(ConnToDB.rs.getString(2));
				a.setProject(proj);
				a.setaStartDate(ConnToDB.rs.getDate(3));
				a.setaEndDate(ConnToDB.rs.getDate(4));
				a.setBillAmount(ConnToDB.rs.getFloat(5));
				a.setAssignType(ConnToDB.rs.getString(6));
				a.setHours(ConnToDB.rs.getInt(7));
				aList.add(a);
			}
			ConnToDB.rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return aList;
	}
	
	public Department getDeptInfo(int empno){
		Department dept = new Department();
		sql = "select d.* from (select deptno from emp where empno = " + empno 
				+ ") e, dept d where d.deptno = e.deptno";
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				dept.setDeptno(ConnToDB.rs.getInt(1));
				dept.setDname(ConnToDB.rs.getString(2));
				dept.setLoc(ConnToDB.rs.getString(3));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return dept;
	}
	
	public Users getUserInfofull(){
		sql = "select ename, mgr, hiredate from emp where empno = " + StaticValue.userLog.getEmpno();
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				StaticValue.userLog.setEname(ConnToDB.rs.getString(1));
				StaticValue.userLog.setMgr(ConnToDB.rs.getInt(2));
				StaticValue.userLog.setHiredate(ConnToDB.rs.getDate(3));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return StaticValue.userLog;
	}
	
	public Users getDirectorInfo(int empno){
		Users user = new Users();
		sql = "select m.empno, m.ename, m.deptno from ( select mgr from emp where empno  = "+ empno +" ) e, emp m"
				+ " where e.mgr = m.empno";
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				user.setEmpno(ConnToDB.rs.getInt(1));
				user.setEname(ConnToDB.rs.getString(2));
				user.setDeptno(ConnToDB.rs.getInt(3));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return user;
	}
}
