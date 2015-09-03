package Controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import Models.Project;
import SQLOracle.ProfileDB;
import SQLOracle.ProjectDB;
import Service.Navigate;
import Service.StaticValue;

@ManagedBean
@SessionScoped
public class ViewProject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Project> projList;
	private List<String> dList;
	private String dept;
	private ProjectDB pdb;

	public List<Project> getProjList() {
		return projList;
	}
	public void setProjList(List<Project> projList) {
		this.projList = projList;
	}
	public List<String> getdList() {
		return dList;
	}
	public void setdList(List<String> dList) {
		this.dList = dList;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}

	public void changeDept(String dept) {
		this.dept = dept;
		getInforProject();
	}

	public void getInforProject() {
		projList = new LinkedList<Project>();
		projList = pdb.getListProject(dept);
	}

	public void getList_UserDept() {
		ProfileDB prdb = new ProfileDB();
		dept = prdb.getDeptInfo(StaticValue.userLog.getEmpno()).getDname();
		dList = new LinkedList<String>();
		pdb = new ProjectDB();
		dList = pdb.getListNameDept();
		dList.add("ALL");
		dList.add("NONE");
	}

	public String getAction() {
		// get action
		getList_UserDept();
		getInforProject();
		Navigate nav = new Navigate();
		return nav.aViewProject();
	}
}
