package SQLOracle;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import Models.Users;
import Service.Job;

public class UserDetailDB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sql;
	
	public Users getUserInfor(int empno){
		Users user = new Users();
		sql = "select ename,job,mgr,hiredate,deptno "
				+ "from emp where empno = " + empno;
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while (ConnToDB.rs.next()) {
				user.setEmpno(empno);
				user.setEname(ConnToDB.rs.getString(1));
				user.setJob(ConnToDB.rs.getString(2));
				user.setMgr(ConnToDB.rs.getInt(3));
				user.setHiredate(ConnToDB.rs.getDate(4));
				user.setDeptno(ConnToDB.rs.getInt(5));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return user;
	}
	
	public List<String> getListJob(String job){
		List<String> lj = new LinkedList<String>();
		if(job.equals(Job.President)){
			sql = "select distinct job from emp";
		}
		else if(job.equals(Job.Manager)){
			sql = "select distinct job from emp where job != 'PRESIDENT' and job != 'MANAGER'";
		}
		else{
			return lj;
		}
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				lj.add(ConnToDB.rs.getString(1));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return lj;
	}
}
