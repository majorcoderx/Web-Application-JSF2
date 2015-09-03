package SQLOracle;

import java.sql.SQLException;

import Service.StaticValue;

public class ChangePassDB {
	private String sql;
	
	public String changePassword(String old,String newp, String rep){
		String msg = "";
		if(!old.equals(StaticValue.userLog.getPassword())||!newp.equals(rep)){
			if(!old.equals(StaticValue.userLog.getPassword())){
				msg = "Old pass not success, try again !";
			}
			if(!newp.equals(rep)){
				msg = "New pass != renew pass, try again !";
			}
		}
		else{
			sql = "update emp set password = " + newp + 
					" where empno = "+StaticValue.userLog.getEmpno();
			try{
				ConnToDB.st.execute(sql);
				StaticValue.userLog.setPassword(newp);
				msg = "Save Success, Thanks !";
			}catch(SQLException ex){
				msg = "Sorry,please try again !";
			}
		}
		return msg;
	}
}
