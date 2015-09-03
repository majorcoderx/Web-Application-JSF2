package Models;

import java.io.Serializable;
import java.util.Date;

public class Assignment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date aStartDate = new Date();
	private Date aEndDate = new Date();
	private float billAmount;
	private String assignType;
	private int hours;
	
	private Users user = new Users();
	private Project project = new Project();

	private boolean join;
	private boolean mgr;
	
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	/*------------------- get - set default---------------*/
	public Date getaStartDate() {
		return aStartDate;
	}
	public void setaStartDate(Date aStartDate) {
		this.aStartDate = aStartDate;
	}
	public Date getaEndDate() {
		return aEndDate;
	}
	public void setaEndDate(Date aEndDate) {
		this.aEndDate = aEndDate;
	}
	public float getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(float billAmount) {
		this.billAmount = billAmount;
	}
	public String getAssignType() {
		return assignType;
	}
	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public boolean isJoin() {
		return join;
	}
	public void setJoin(boolean join) {
		this.join = join;
	}
	public boolean isMgr() {
		return mgr;
	}
	public void setMgr(boolean mgr) {
		this.mgr = mgr;
	}
}
