package Models;

import java.io.Serializable;

public class DeptJoin implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Department dept = new Department();
	private boolean join;
	
	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
	}
	public boolean isJoin() {
		return join;
	}
	public void setJoin(boolean join) {
		this.join = join;
	}
}
