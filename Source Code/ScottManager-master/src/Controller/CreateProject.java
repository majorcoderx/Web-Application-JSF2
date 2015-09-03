package Controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ActionObject.ManagerCompany;
import Models.Assignment;
import Models.Department;
import Models.DeptJoin;
import Models.Project;
import SQLOracle.DeptDB;
import SQLOracle.NewProjectDB;
import SQLOracle.ProjectDetailDB;
import Service.Navigate;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CreateProject implements Serializable, ManagerCompany {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Project proj;
	private List<DeptJoin> djList;
	private List<Assignment> assignList;

	private Assignment asgSelected =  new Assignment();
	private int noStaffSelected;
	private List<Integer> noStaffList;
	private ProjectDetailDB pdb;
	private NewProjectDB ndb;

	public int getNoStaffSelected() {
		return noStaffSelected;
	}
	public void setNoStaffSelected(int noStaffSelected) {
		this.noStaffSelected = noStaffSelected;
	}
	public List<Integer> getNoStaffList() {
		return noStaffList;
	}
	public void setNoStaffList(List<Integer> noStaffList) {
		this.noStaffList = noStaffList;
	}
	public Assignment getAsgSelected() {
		return asgSelected;
	}
	public void setAsgSelected(Assignment asgSelected) {
		this.asgSelected = asgSelected;
	}
	public Project getProj() {
		return proj;
	}
	public void setProj(Project proj) {
		this.proj = proj;
	}
	public List<DeptJoin> getDjList() {
		return djList;
	}
	public void setDjList(List<DeptJoin> djList) {
		this.djList = djList;
	}
	public List<Assignment> getAssignList() {
		return assignList;
	}
	public void setAssignList(List<Assignment> assignList) {
		this.assignList = assignList;
	}
	
	public void getProjectInfo() {
		proj = new Project();
		proj = ndb.randomProject();
		noStaffSelected = proj.getMaxStaff();
	}

	public void getDepartmentsInfo() {
		DeptDB db = new DeptDB();
		djList = new LinkedList<DeptJoin>();
		List<Department> dList = new LinkedList<Department>(db.getDeptList());
		for(int i= 0;i<dList.size();++i){
			DeptJoin dj = new DeptJoin();
			dj.setDept(dList.get(i));
			dj.setJoin(false);
			djList.add(dj);
		}
	}

	public String getAction() {
		pdb = new ProjectDetailDB();
		ndb = new NewProjectDB();
		assignList = new ArrayList<Assignment>();
		getDepartmentsInfo();
		getProjectInfo();
		Navigate nav = new Navigate();
		return nav.aViewCreateNewProject();
	}

	public void changeMaxNoStaff() {
		proj.setMaxStaff(noStaffSelected);
	}

	private boolean checkAvailable;

	public boolean isCheckAvailable() {
		int count = 0;
		for (int i = 0; i < assignList.size(); ++i) {
			if (assignList.get(i).isJoin()) {
				++count;
			}
		}
		if (count < proj.getMaxStaff()) {
			checkAvailable = true;
		} else {
			checkAvailable = false;
		}
		return checkAvailable;
	}

	public void setCheckAvailable(boolean checkAvailable) {
		this.checkAvailable = checkAvailable;
	}

	public void changeUserJoinAction(int empno, boolean join) {
		if (join) {
			for (int i = 0; i < assignList.size(); ++i) {
				if (empno == assignList.get(i).getUser().getEmpno()) {
					assignList.get(i).setJoin(true);
				}
			}
		} else {
			for (int i = 0; i < assignList.size(); ++i) {
				if (empno == assignList.get(i).getUser().getEmpno()) {
					assignList.get(i).setJoin(false);
				}
			}
		}
		changeMaxNoSatff();
	}

	public void changeMaxNoSatff() {
		noStaffList = new LinkedList<Integer>();
		int maxNo = assignList.size();
		int minNo = 0;
		for (int i = 0; i < maxNo; ++i) {
			if (assignList.get(i).isJoin()) {
				++minNo;
			}
		}
		for (int i = minNo; i <= maxNo; ++i) {
			noStaffList.add(new Integer(i));
		}
	}

	public void getSaveAction() {
		List<Assignment> asgList = new LinkedList<Assignment>();
		for (int i = 0; i < assignList.size(); ++i) {
			if (assignList.get(i).isJoin()) {
				assignList.get(i).setProject(proj);
				asgList.add(assignList.get(i));
			}
		}
		String msg = ndb.createNewProject(proj);
		for(int  i =0;i < asgList.size();++i){
			pdb.createNewAssign(asgList.get(i));
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("", msg));
	}

	public void addNewDepartment(int deptno, boolean join) {
		if (join) {
			ProjectDetailDB pdb = new ProjectDetailDB();
			assignList.addAll(pdb.getUserOfDept(deptno));
		} else {
			int size = assignList.size();
			while (size > 0) {
				if (assignList.get(size - 1).getUser().getDeptno() == deptno) {
					assignList.remove(size - 1);
				}
				--size;
			}
		}
		changeMaxNoSatff();
	}
	
	public void updateAssignList(){
		if(asgSelected.isJoin()){
			for(int i = 0;i < assignList.size();++i){
				if(asgSelected.getUser().getEmpno() == assignList.get(i).getUser().getEmpno()){
					if(asgSelected.getaStartDate() == asgSelected.getaEndDate()){
						assignList.get(i).setJoin(false);
					}
					else{
						assignList.get(i).setaStartDate(asgSelected.getaStartDate());
						assignList.get(i).setaEndDate(asgSelected.getaEndDate());
						assignList.get(i).setHours(asgSelected.getHours());
						assignList.get(i).setBillAmount(asgSelected.getBillAmount());
						if(asgSelected.getAssignType() == null){
							asgSelected.setAssignType("");
						}
						assignList.get(i).setAssignType(asgSelected.getAssignType());
					}
				}
			}
		}
	}
	@Override
	public String getAction(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
