package Controller;

//import java.io.Serializable;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import Models.*;
import ActionObject.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import SQLOracle.DeptDB;
import SQLOracle.ProfileDB;
import Service.*;

@ManagedBean
@SessionScoped
public class ViewProfile implements Serializable, ManagerCompany {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProfileDB pdb;
	private Users userLog = StaticValue.userLog;
	private String mgrName;
	private Department dept;
	private String dname;
	private List<String> dList;// list departent name
	private List<String> ldir;//danh sach quan ly
	private String dirName;
	private List<Assignment> aList; // list assignment
	
	public ViewProfile() {
		super();
		
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}

	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
	}
	public String getMgrName() {
		return mgrName;
	}
	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}
	public Users getUserLog() {
		return userLog;
	}
	public void setUserLog(Users userLog) {
		this.userLog = userLog;
	}
	public List<String> getLdir() {
		return ldir;
	}
	public void setLdir(List<String> ldir) {
		this.ldir = ldir;
	}
	public String getDirName() {
		return dirName;
	}
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	public List<Assignment> getaList() {
		return aList;
	}
	public void setaList(List<Assignment> aList) {
		this.aList = aList;
	}
	public List<String> getdList() {
		return dList;
	}
	public void setdList(List<String> dList) {
		this.dList = dList;
	}
	
	public void getUserInfo(int empno) {
		pdb.getUserInfofull();
		dept = new Department();
		dept = pdb.getDeptInfo(empno);
		mgrName = pdb.getManagerNameFromDname(dept.getDname());
		DeptDB ddb = new DeptDB();
		dList = new LinkedList<String>();
		dList = ddb.getListNameDept();	
		dname = dept.getDname();
	}

	public void getProjectInfo() {
		aList = new LinkedList<Assignment>();
		aList = pdb.getProjectInfo(StaticValue.userLog.getEmpno());
	}

	public void getSaveAction() {
		FacesContext context = FacesContext.getCurrentInstance();
		userLog.setDeptno(dept.getDeptno());
		if(pdb.saveToDB(userLog)){
			context.addMessage(null, new FacesMessage("Update success", "Good!"));
		}
		else{
			context.addMessage(null, new FacesMessage("Update error", "Try again !"));
		}
	}

	public void changeDept(String dname) {
		
		mgrName = pdb.getManagerNameFromDname(dname);
		dept = pdb.getDeptFromName(dname);
	}
	
	public String getAction() {
		pdb = new ProfileDB();
		getUserInfo(StaticValue.userLog.getEmpno());
		getProjectInfo();
		Navigate nav = new Navigate();
		return nav.aViewProfile();
	}
	@Override
	public String getAction(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}