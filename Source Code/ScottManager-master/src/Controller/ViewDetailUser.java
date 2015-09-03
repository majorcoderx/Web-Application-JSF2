package Controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import ActionObject.ManagerCompany;
import Models.Assignment;
import Models.Department;
import Models.Users;
import SQLOracle.DeptDB;
import SQLOracle.EmpDB;
import SQLOracle.ProfileDB;
import SQLOracle.UserDetailDB;
import Service.Authories;
import Service.Navigate;
import Service.StaticValue;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ViewDetailUser implements Serializable, ManagerCompany {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UserDetailDB udb;
	private ProfileDB pdb;
	private EmpDB edb;
	
	private Users user;
	private Department dept;
	private String dname;
	private List<String> dList;
	private Users dir;
	private String mgr;
	private List<Users> listDir;
	private List<Assignment> aList;
	private String job;
	private List<String> jlist;
	private boolean isDirector;
	
	public boolean isDirector() {
		Authories au = new Authories();
		isDirector = au.isDir(user.getMgr());
		return isDirector;
	}
	public void setDirector(boolean isDirector) {
		this.isDirector = isDirector;
	}
	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public List<String> getdList() {
		return dList;
	}
	public void setdList(List<String> dList) {
		this.dList = dList;
	}	
	public Users getDir() {
		return dir;
	}
	public void setDir(Users dir) {
		this.dir = dir;
	}	
	public String getMgr() {
		return mgr;
	}
	public void setMgr(String mgr) {
		this.mgr = mgr;
	}
	public List<Assignment> getaList() {
		return aList;
	}
	public void setaList(List<Assignment> aList) {
		this.aList = aList;
	}
	
	public List<Users> getListDir() {
		return listDir;
	}
	public void setListDir(List<Users> listDir) {
		this.listDir = listDir;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public List<String> getJlist() {
		return jlist;
	}
	public void setJlist(List<String> jlist) {
		this.jlist = jlist;
	}
	public void getInforUser(int empno){
		user = new Users(udb.getUserInfor(empno));
		dept = new Department(pdb.getDeptInfo(empno));
		dname = dept.getDname();
		DeptDB ddb = new DeptDB();
		dList = new LinkedList<String>(ddb.getListNameDept());
		mgr = pdb.getManagerNameFromDname(dname);
		//System.out.println("MANAGER USER: "+mgr);
		dir = new Users(pdb.getDirectorInfo(empno));
		listDir = new LinkedList<Users>(edb.getListDirector(user.getEmpno()));
		job = user.getJob();
		jlist = new LinkedList<String>(udb.getListJob(StaticValue.userLog.getJob()));
	}
	
	public void changeDept(String dname){
		mgr = pdb.getManagerNameFromDname(dname);
		dept = pdb.getDeptFromName(dname);
	}
	
	public void changeDirector(Users user){
		System.out.println("USER DIRECTOR: "+ user.getEmpno());
	}
	
	public void getProjectInfor(){
		aList = new LinkedList<Assignment>();
		aList = pdb.getProjectInfo(user.getEmpno());
	}
	
	public void changeJob(String job){
		System.out.println("JOB "+job);
	}
	
	public void saveAction(){
		FacesContext context = FacesContext.getCurrentInstance();
		user.setDeptno(dept.getDeptno());
		if(pdb.saveToDB(user)){
			context.addMessage(null, new FacesMessage("Update success", "Good!"));
		}
		else{
			context.addMessage(null, new FacesMessage("Update error", "Try again !"));
		}
	}
	
	@Override
	public String getAction() {
		return null;
	}
	@Override
	public String getAction(int empno) {
		// TODO Auto-generated method stub
		udb = new UserDetailDB();
		pdb = new ProfileDB();
		edb = new EmpDB();
		getInforUser(empno);
		getProjectInfor();
		Navigate nav = new Navigate();
		return nav.aViewDetailUser();
	}
	
}
