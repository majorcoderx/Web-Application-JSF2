package Controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import SQLOracle.ChangePassDB;

@ManagedBean
@SessionScoped
public class ChangePass implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String oldPass ="";
	private String newPass = "";
	private String renewPass = "";
	public String getOldPass() {
		return oldPass;
	}
	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}
	public String getNewPass() {
		return newPass;
	}
	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}
	public String getRenewPass() {
		return renewPass;
	}
	public void setRenewPass(String renewPass) {
		this.renewPass = renewPass;
	}
	
	public void changePassword(){
		ChangePassDB cp = new ChangePassDB();
		String msg = cp.changePassword(oldPass, newPass, renewPass);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Notify",msg));
	}
	
	public String resetPass(){
		setOldPass("");
		setNewPass("");
		setRenewPass("");
		return "";
	}
}
