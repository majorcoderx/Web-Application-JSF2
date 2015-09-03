package SQLOracle;

import Models.Project;
import Service.DeptDefine;
import Service.Job;
import Service.StaticValue;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

public class ProjectDB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sql;
	
	public List<Project> getListProject(String dept){
		List<Project> lproj = new LinkedList<Project>();
		if(StaticValue.userLog.getJob().equals(Job.President)){
			if(!dept.equals(DeptDefine.All) && !dept.equals(DeptDefine.None)){
				sql = "select distinct p.* from "
						+ "(select e.empno from emp e, dept d where d.dname = '"
						+ dept + "' and e.deptno = d.deptno ) e "
						+ ", project p, assignments a "
						+ "where e.empno = a.empno and p.projid = a.projid";
			}
			else if(dept.equals(DeptDefine.All)){
				sql = "select distinct p.* from (select projid from assignments) p_a, project p "
						+ "where p_a.projid = p.projid";
			}
			else{
				sql ="select p.* from project p "
						+ "where p.projid not in (select distinct projid from assignments)";
			}
		}
		else if(StaticValue.userLog.getJob().equals(Job.Manager)){
			sql = "select distinct p.* from "
					+ "(select e.empno from emp e, dept d where d.dname = '"
					+ dept + "' and e.deptno = d.deptno ) e "
					+ ", project p, assignments a "
					+ "where e.empno = a.empno and p.projid = a.projid";
		}else{
			sql = "select distinct p.* "
					+ "from (select a.projid projid from ASSIGNMENTS a, emp e where e.empno = "
					+ StaticValue.userLog.getEmpno()
					+ " and a.empno = e.EMPNO) a, project p"
					+ " where a.projid = p.projid";
		}
		
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while (ConnToDB.rs.next()) {
				Project project = new Project();
				project.setProjId(ConnToDB.rs.getInt(1));
				project.setDescrible(ConnToDB.rs.getString(2));
				project.setStartDate(ConnToDB.rs.getDate(3));
				project.setEndDate(ConnToDB.rs.getDate(4));
				project.setBudgetAmount(ConnToDB.rs.getInt(5));
				project.setMaxStaff(ConnToDB.rs.getInt(6));
				project.setComment(ConnToDB.rs.getInt(7));
				lproj.add(project);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return lproj;
	}
	
	public List<String> getListNameDept(){
		sql = "select dname from dept";
		List<String> ldname = new LinkedList<String>();
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				ldname.add(ConnToDB.rs.getString(1));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return ldname;
	}
}
