package SQLOracle;

import java.util.*;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Models.Assignment;
import Models.Department;
import Models.DeptJoin;
import Models.Project;
import Models.Users;
import Service.Job;
import Service.StaticValue;

public class ProjectDetailDB implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sql;
	private PreparedStatement pr = null;
	
	public Project getProjectDetailInfo(int projid){
		Project proj = new Project();
		sql = "select p_desc,p_start_date,p_end_date,budget_amount,max_no_staff,comments"
				+ " from project where projid = " + projid;
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				proj.setProjId(projid);
				proj.setDescrible(ConnToDB.rs.getString(1));
				proj.setStartDate(ConnToDB.rs.getDate(2));
				proj.setEndDate(ConnToDB.rs.getDate(3));
				proj.setBudgetAmount(ConnToDB.rs.getInt(4));
				proj.setMaxStaff(ConnToDB.rs.getInt(5));
				proj.setComment(ConnToDB.rs.getInt(6));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return proj;
	}
	
	public List<DeptJoin> getListDeptJoin(int projid){
		List<DeptJoin> ldept = new LinkedList<DeptJoin>();
		try{
			sql = "select DISTINCT d.deptno, d.dname, d.loc"
					+ " from  dept d, emp e, assignments a " 
					+ "where a.projid = "+ projid + " and e.empno = a.empno and d.deptno = e.deptno";
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while (ConnToDB.rs.next()) {
				Department dept = new Department();
				dept.setDeptno(ConnToDB.rs.getInt(1));
				dept.setDname(ConnToDB.rs.getString(2));
				dept.setLoc(ConnToDB.rs.getString(3));
				DeptJoin dj = new DeptJoin();
				dj.setDept(dept);
				dj.setJoin(true);
				ldept.add(dj);
			}
			sql = "select d.deptno, d.dname, d.loc from dept d "
					+ "where d.deptno not in (select distinct e.deptno deptno from ASSIGNMENTS a, emp e "
					+ "where a.empno = e.empno and a.projid = " + projid + ")";
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while (ConnToDB.rs.next()) {
				Department dept = new Department();
				dept.setDeptno(ConnToDB.rs.getInt(1));
				dept.setDname(ConnToDB.rs.getString(2));
				dept.setLoc(ConnToDB.rs.getString(3));
				DeptJoin dj = new DeptJoin();
				dj.setDept(dept);
				dj.setJoin(false);
				ldept.add(dj);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return ldept;
	}
	
	public List<Assignment> getStaffForProject(Project project){
		List<Assignment> asgList = new LinkedList<Assignment>();
		try{
			sql = "select e.deptno, e.empno, e.ename, e.job, a.a_start_date, a.a_end_date,a.bill_amount,a.assign_type,a.hours "
					+ "from emp e, assignments a "
					+ "where a.projid = "+ project.getProjId() + " and e.empno = a.empno";
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while (ConnToDB.rs.next()) {
				Users user = new Users();
				user.setDeptno(ConnToDB.rs.getInt(1));
				user.setEmpno(ConnToDB.rs.getInt(2));
				user.setEname(ConnToDB.rs.getString(3));
				user.setJob(ConnToDB.rs.getString(4));
				Assignment asg = new Assignment();
				asg.setUser(user);
				asg.setProject(project);
				asg.setaStartDate(ConnToDB.rs.getDate(5));
				asg.setaEndDate(ConnToDB.rs.getDate(6));
				asg.setBillAmount(ConnToDB.rs.getFloat(7));
				asg.setAssignType(ConnToDB.rs.getString(8));
				asg.setHours(ConnToDB.rs.getInt(9));
				asg.setJoin(true);
				asgList.add(asg);
			}
			
			sql = "select e.deptno, e.empno,e.ename, e.job "
					+ "from (select DISTINCT e.deptno from emp e, ASSIGNMENTS a where e.EMPNO = a.EMPNO and a.PROJID = "+ 
					project.getProjId() + " ) dept_proj, emp e where e.DEPTNO = dept_proj.deptno and e.empno not in "
							+ "(select empno from ASSIGNMENTS where projid = "+ project.getProjId() + ")";
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while (ConnToDB.rs.next()) {
				Users user = new Users();
				user.setDeptno(ConnToDB.rs.getInt(1));
				user.setEmpno(ConnToDB.rs.getInt(2));
				user.setEname(ConnToDB.rs.getString(3));
				user.setJob(ConnToDB.rs.getString(4));
				Assignment asg = new Assignment();
				asg.setUser(user);
				asg.setJoin(false);
				asgList.add(asg);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return asgList;
	}
	
	public boolean saveIntoDB(){
		boolean check = false;
		
		return check;
	}
	
	public List<Integer> getListEmpnoProject(int projid) {
		List<Integer> lI = new LinkedList<Integer>();
		sql = "select empno from assignments where projid = " + projid;
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				lI.add(ConnToDB.rs.getInt(1));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return lI;
	}
	
	public boolean updateStaffOfProject(Assignment asg){
		boolean check = false;
		sql = "Update assignments "
				+ "set a_start_date = ?, a_end_date = ?, bill_amount= ?, assign_type = ?, hours = ?"
				+ "where empno = ? and projid = "+ asg.getProject().getProjId();
		java.sql.Date sDate = new java.sql.Date(asg.getaStartDate().getTime());
		java.sql.Date edate = new java.sql.Date(asg.getaEndDate().getTime());
		try{
			pr = ConnToDB.conn.prepareStatement(sql);
			pr.setDate(1, sDate);
			pr.setDate(2, edate);
			pr.setFloat(3, asg.getBillAmount());
			pr.setString(4, asg.getAssignType());
			pr.setFloat(5, asg.getHours());
			pr.setInt(6, asg.getUser().getEmpno());
			int ucheck = pr.executeUpdate();
			if(ucheck > 0){
				check = true;
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return check;
	}
	
	public boolean createNewAssign(Assignment asg){
		boolean check = false;
		sql = "insert into "
				+ "assignments(projid,empno,a_start_date,a_end_date,bill_amount,assign_type,hours) "
				+ "values(?,?,?,?,?,?,?)";
		java.sql.Date sdate = new java.sql.Date(asg.getaStartDate().getTime());
		java.sql.Date edate = new java.sql.Date(asg.getaEndDate().getTime());
		try{
			pr = ConnToDB.conn.prepareStatement(sql);
			pr.setInt(1, asg.getProject().getProjId());
			pr.setInt(2, asg.getUser().getEmpno());
			pr.setDate(3, sdate);
			pr.setDate(4, edate);
			pr.setFloat(5, asg.getBillAmount());
			pr.setString(6, asg.getAssignType());
			pr.setInt(7, asg.getHours());
			int icheck = pr.executeUpdate();
			if(icheck > 0){
				check = true;
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return check;
	}
	
	public String updateInfoProject(Project proj){
		String msg = "";
		try{
			if(StaticValue.userLog.getJob().equals(Job.President)){
				sql = "update project "
						+ "set p_desc = ?,p_start_date = ?, p_end_date = ?,budget_amount = ?,max_no_staff = ?, comments = ?"
						+ " where projid = " + proj.getProjId();
				java.sql.Date sDate = new java.sql.Date(proj.getStartDate().getTime());
				java.sql.Date edate = new java.sql.Date(proj.getEndDate().getTime());
				pr = ConnToDB.conn.prepareStatement(sql);
				pr.setString(1, proj.getDescrible());
				pr.setDate(2, sDate);
				pr.setDate(3, edate);
				pr.setInt(4, proj.getBudgetAmount());
				pr.setInt(5, proj.getMaxStaff());
				pr.setInt(6, proj.getComment());
				int udate = pr.executeUpdate();
				if(udate > 0){
					msg = "Update Project Success";
				}
			}
			if(StaticValue.userLog.getJob().equals(Job.Manager)){
				sql = "update project "
						+ "set max_no_staff = ?, comments = ? "
						+ "where projid = " + proj.getProjId();
				pr = ConnToDB.conn.prepareStatement(sql);
				pr.setInt(1, proj.getMaxStaff());
				pr.setInt(2, proj.getComment());
				int udate = pr.executeUpdate();
				if(udate > 0){
					msg = "Update Project Success";
				}
			}
		}catch(SQLException ex){
			ex.printStackTrace();
			msg = "Update Project Fail !";
		}		
		return msg;
	}
	
	public String deleteAssign(int empno, int projid){
		String msg = "";
		sql = "Delete from assignments where empno = ? and projid  = ?";
		try{
			pr = ConnToDB.conn.prepareStatement(sql);
			pr.setInt(1, empno);
			pr.setInt(2, projid);
			int ucheck = pr.executeUpdate();
			if(ucheck > 0 ){
				msg = "delete assign success";
			}
			else{
				msg = "delete assign fail";
			}
		}catch(SQLException ex){
			ex.printStackTrace();
			msg = "delete assign fail";
		}
		return msg;
	}
	
	public List<Assignment> getUserOfDept(int deptno){
		List<Assignment> alist = new LinkedList<Assignment>();
		sql = "select empno, ename, job from emp where deptno = " + deptno;
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while (ConnToDB.rs.next()) {
				Users user = new Users();
				user.setDeptno(deptno);
				user.setEmpno(ConnToDB.rs.getInt(1));
				user.setEname(ConnToDB.rs.getString(2));
				user.setJob(ConnToDB.rs.getString(3));
				Assignment asg = new Assignment();
				asg.setUser(user);
				alist.add(asg);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return alist;
	}
}
