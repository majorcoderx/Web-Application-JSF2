package Service;

import java.io.Serializable;
import java.sql.SQLException;

import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import SQLOracle.ConnToDB;

@ManagedBean
@SessionScoped
public class Navigate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String aViewProfile(){
		return "/Views/Share/profile.xhtml?faces-redirect=true";
	}
	
	public String aChangePassword(){
		return "/Views/Share/changepassword.xhtml?faces-redirect=true";
	}
	
	public String aViewEmp(){
		return "/Views/Share/employee.xhtml?faces-redirect=true";
	}
	
	public String aViewProject(){
		return "/Views/Share/project.xhtml?faces-redirect=true";
	}
	
	public String getHomeAction(){
		if(StaticValue.userLog.getJob().equals(Job.President)){
			return "/Views/Personalized/prehome.xhtml?faces-redirect=true";
		}
		else if(StaticValue.userLog.getJob().equals(Job.Manager)){
			return "/Views/Personalized/manhome.xhtml?faces-redirect=true";
		}
		else{
			return "/Views/Personalized/otherhome.xhtml?faces-redirect=true";
		}
	}
	
	public String aViewDetailProject(){
		return "/Views/Share/detailproject.xhtml?faces-redirect=true";
	}
	
	public String aViewCreateNewProject(){
		return "/Views/Share/createnewproject.xhtml?faces-redirect=true";
	}
	
	public String aViewDetailUser(){
		return "/Views/Share/viewdetailuser.xhtml?faces-redirect=true";
	}
	
	public String logout(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); //remove session
		StaticValue.userLog = null;
		StaticValue.isLogged = false;
		try{
			ConnToDB.st.close();
			ConnToDB.conn.close();
		}catch(SQLException ex){
			System.out.println("Dong ket noi vao database that bai !");
		}
		return "/Views/Share/index.xhtml?faces-redirect=true";
	}
}
