package Controller;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import Models.Users;
import SQLOracle.ConnToDB;
import Service.DeptDefine;
import Service.Job;
import Service.Navigate;
import Service.StaticValue;

@ManagedBean
@SessionScoped
public class EmpManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Users> uList;
	private List<String> deptList;
	private String dept;
	private String selectedDept;
	
	private Users selectedUser;
	
	public List<Users> getuList() {
		return uList;
	}
	public void setuList(List<Users> uList) {
		this.uList = uList;
	}
	public String getSelectedDept() {
		return selectedDept;
	}
	public void setSelectedDept(String selectedDept) {
		this.selectedDept = selectedDept;
	}
	public List<String> getDeptList() {
		return deptList;
	}
	public void setDeptList(List<String> deptList) {
		this.deptList = deptList;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public Users getSelectedUser() {
		return selectedUser;
	}
	public void setSelectedUser(Users selectedUser) {
		this.selectedUser = selectedUser;
	}

	public void getInfoUser(String sql){
		uList = new LinkedList<Users>();
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				Users user = new Users();
				user.setEmpno(ConnToDB.rs.getInt(1));
				user.setEname(ConnToDB.rs.getString(2));
				user.setJob(ConnToDB.rs.getString(3));
				user.setMgr(ConnToDB.rs.getInt(4));
				user.setHiredate(ConnToDB.rs.getDate(5));
				uList.add(user);
			}
		}catch(SQLException ex){
			
		}
	}
	
	public void controlAccess(){
		if(StaticValue.userLog.getJob().equals(Job.President)){
			//view all user with department choose
			if(getSelectedDept().equals(DeptDefine.All)){
				String sql = "select empno, ename, job, mgr, hiredate from emp";
				getInfoUser(sql);
			}
			else{
				String sql = "select e.empno, e.ename, e.job, e.mgr, e.hiredate "
						+ "from (select deptno from dept where dname = '"+ getSelectedDept()+"') d, emp e "
						+ "where d.deptno = e.deptno";
				getInfoUser(sql);
			}
		}
		else if(StaticValue.userLog.getJob().equals(Job.Manager)){
			//view list user in department
			String sql = "select empno, ename, job, mgr, hiredate "
					+ "from emp"
					+ "where deptno = "+StaticValue.userLog.getDeptno();
			getInfoUser(sql);
		}
		else{
			//xem nhung nguoi maf ng nay quan ly
			String sql = "select empno, ename, job, mgr, hiredate "
					+ "from emp where mgr = "+StaticValue.userLog.getEmpno();
			getInfoUser(sql);
		}
		//test
		for(int i = 0;i< uList.size();++i){
			System.out.println(uList.get(i).getEname());
		}
	}
	
	public String getDetpUser() {
		String sql = "select dname "
				+ "from (select deptno from emp where empno = "
				+ StaticValue.userLog.getEmpno() + ") dep_no, dept d"
				+ " where dep_no.deptno = d.deptno";
		String dname = DeptDefine.All;
		try {
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while (ConnToDB.rs.next()) {
				dname = ConnToDB.rs.getString(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("Error at detDeptuser() !");
		}
		return dname;
	}

	public String getAction(){
		//get action in here
		setSelectedDept(getDetpUser());
		controlAccess();
		Navigate nav = new Navigate();
		return nav.aViewEmp();
	}
}
