package Models;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Department implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int deptno;
	private String dname;
	private String loc;
	
	/*------set get----------*/
	public Department(){
		
	}
	public Department(Department dept){
		this.deptno = dept.deptno;
		this.dname = dept.dname;
		this.loc = dept.loc;
	}
	
	public int getDeptno() {
		return deptno;
	}
	public void setDeptno(int deptno) {
		this.deptno = deptno;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getLoc() {
		return loc;
	}	
	public void setLoc(String loc) {
		this.loc = loc;
	}
}
