package Controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import ActionObject.ManagerCompany;
import Models.*;
import SQLOracle.ProjectDetailDB;
import Service.Authories;
import Service.Job;
import Service.Navigate;
import Service.StaticValue;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ViewDetailProject implements Serializable, ManagerCompany {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private Assignment assign;
	private Project proj;
	private List<DeptJoin> djList;
	private List<Assignment> assignList;
	private Assignment asgSelected =  new Assignment();
	private int noStaffSelected;
	private List<Integer> noStaffList;
	
	private boolean checkAvailable;
	private ProjectDetailDB pdb;
	private boolean isMgrProj;
	
	public boolean isMgrProj() {
		Authories au = new Authories();
		isMgrProj = au.mgrProj(proj.getProjId());
		return isMgrProj;
	}
	public void setMgrProj(boolean isMgrProj) {
		this.isMgrProj = isMgrProj;
	}

	public boolean isCheckAvailable() {
		int count = 0;
		int countMaxU = 0;
		for (int i = 0; i < assignList.size(); ++i) {
			if (assignList.get(i).isJoin()) {
				++count;
			}
			++countMaxU;
		}
		if (count < proj.getMaxStaff()||proj.getMaxStaff() == countMaxU) {
			checkAvailable = true;
		} else {
			checkAvailable = false;
		}
		return checkAvailable;
	}
	public void setCheckAvailable(boolean checkAvailable) {
		this.checkAvailable = checkAvailable;
	}
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

	public void getProjectInfo(int projid) {
		proj = new Project();
		proj = pdb.getProjectDetailInfo(projid);
		noStaffSelected = proj.getMaxStaff();
	}

	public void getDepartmentsInfo() {
		djList = new LinkedList<DeptJoin>();
		djList = pdb.getListDeptJoin(proj.getProjId());
	}

	public void getStaffInProjectInfo() {
		assignList = new LinkedList<Assignment>();
		assignList = pdb.getStaffForProject(proj);
		changeMaxNoSatff();
	}


	public void changeMaxNoStaff() {
		proj.setMaxStaff(noStaffSelected);
	}

	public void changeUserJoinAction(int empno, boolean join) {
		if (join) {
			for (int i = 0; i < assignList.size(); ++i) {
				if (empno == assignList.get(i).getUser().getEmpno()) {
					assignList.get(i).setJoin(true);
					assignList.get(i).setProject(proj);
				}
			}
		} else {
			for (int i = 0; i < assignList.size(); ++i) {
				if (empno == assignList.get(i).getUser().getEmpno()) {
					assignList.get(i).setJoin(false);
					assignList.get(i).setProject(null);
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
		List<Assignment> asgList = new LinkedList<Assignment>(); // list user -															// project
		for (int i = 0; i < assignList.size(); ++i) {
			if (assignList.get(i).isJoin()) {
				asgList.add(assignList.get(i));
			}
		}
		List<Integer> eAsgDb = new LinkedList<Integer>();
		eAsgDb = pdb.getListEmpnoProject(proj.getProjId());	
		List<Integer> compList = new LinkedList<Integer>();
			
		int sizeEAD = eAsgDb.size();
		int sizeAL = asgList.size();
		if (sizeEAD > sizeAL) {
			for (int i = 0; i < sizeEAD; ++i) {
				for (int j = 0; j < sizeAL; ++j) {
					if (eAsgDb.get(i) == asgList.get(j).getUser().getEmpno()) { // update .
						pdb.updateStaffOfProject(asgList.get(j));
						compList.add(eAsgDb.get(i));
					}
				}
			}
		} else {
			for (int i = 0; i < sizeAL; ++i) {
				for (int j = 0; j < sizeEAD; ++j) {
					if (eAsgDb.get(j) == asgList.get(i).getUser().getEmpno()) {
						pdb.updateStaffOfProject(asgList.get(i));
						compList.add(eAsgDb.get(j));
					}
				}
			}
		}
		boolean check;
		for (int i = 0; i < sizeEAD; ++i) {
			check = true;
			for (int j = 0; j < compList.size(); ++j) {
				if (eAsgDb.get(i) == compList.get(j)) {
					check = false;
				}
			}
			if (check) {
				pdb.deleteAssign(eAsgDb.get(i), proj.getProjId());
			}
		} 
		for (int i = 0; i < sizeAL; ++i) {
			check = true;
			for (int j = 0; j < compList.size(); ++j) {
				if (asgList.get(i).getUser().getEmpno() == compList.get(j)) {
					check = false;
				}
			}
			if (check) {
				pdb.createNewAssign(asgList.get(i));
			}
		} // update project table
		FacesContext context = FacesContext.getCurrentInstance();
		if (StaticValue.userLog.getJob().equals(Job.President)) {
			String msg = pdb.updateInfoProject(proj);
			context.addMessage(null, new FacesMessage("", msg));
		}
		if (StaticValue.userLog.getJob().equals(Job.Manager)) {
			String msg = pdb.updateInfoProject(proj);
			context.addMessage(null, new FacesMessage("", msg));
		}
	}

	public void addNewDepartment(int deptno, boolean join) {
		if (join) {
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
	public String getAction() {
		return null;
	}
	
	@Override
	public String getAction(int projid) {
		// TODO Auto-generated method stub
		pdb = new ProjectDetailDB();
		getProjectInfo(projid);
		getDepartmentsInfo();
		getStaffInProjectInfo();
		Navigate nav = new Navigate();
		return nav.aViewDetailProject();
	}
}
