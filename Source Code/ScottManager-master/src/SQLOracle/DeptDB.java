package SQLOracle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Models.Department;

public class DeptDB {
	private String sql;
	
	public List<String> getListNameDept(){
		List<String> ldept = new ArrayList<String>();
		sql = "select dname from dept";
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				ldept.add(ConnToDB.rs.getString(1));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return ldept;
	}
	
	public List<String> getListLocDept(){
		List<String> lloc = new ArrayList<String>();
		sql = "select loc from dept";
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				lloc.add(ConnToDB.rs.getString(1));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return lloc;
	}
	
	public List<Department> getDeptList(){
		List<Department> ldept = new LinkedList<Department>();
		sql = "select * from dept";
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				Department dept = new Department();
				dept.setDeptno(ConnToDB.rs.getInt(1));
				dept.setDname(ConnToDB.rs.getString(2));
				dept.setLoc(ConnToDB.rs.getString(3));
				ldept.add(dept);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return ldept;
	}
}
