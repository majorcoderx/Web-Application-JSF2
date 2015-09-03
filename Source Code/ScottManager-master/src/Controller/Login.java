package Controller;

import SQLOracle.LoginDB;
import Service.*;
import ActionObject.*;

import javax.faces.bean.ManagedBean;  
import javax.faces.bean.SessionScoped;

import java.io.Serializable;

@ManagedBean
@SessionScoped
public class Login implements Serializable, ManagerCompany  {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = "";
	private String password = "";
	private String msg;

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/*--------------- process in here-------------*/
	
	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		SQLOracle.LoginDB lgdb = new LoginDB();
		if(!id.equals("")&&!password.equals("")){
			lgdb.getUser(Integer.parseInt(id), password);
		}
		if(StaticValue.isLogged){
			if(StaticValue.userLog.getJob().equals(Job.President)){
				return "/Views/Personalized/prehome.xhtml?faces-redirect=true";
			}
			else if(StaticValue.userLog.getJob().equals(Job.Manager)){
				return "/Views/Personalized/manhome.xhtml?faces-redirect=true";
			}
			else return "/Views/Personalized/otherhome.xhtml?faces-redirect=true";
		}
		else{
			if(id.equals("")  || password.equals("")){
				msg = "Check your password or ID";
			}
			return "";
		}
	}
	@Override
	public String getAction(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
