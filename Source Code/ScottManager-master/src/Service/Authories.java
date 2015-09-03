package Service;

import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import SQLOracle.ConnToDB;

@ManagedBean
@SessionScoped
public class Authories {

	public boolean isPre() {
		if (StaticValue.userLog.getJob().equals(Job.President)) {
			return true;
		} else
			return false;
	}

	public boolean isMgr() {
		if (StaticValue.userLog.getJob().equals(Job.Manager)
				|| StaticValue.userLog.getJob().equals(Job.President)) {
			return true;
		} else
			return false;
	}

	public boolean mgrProj(int projid) { //manager cua project do
		if (StaticValue.userLog.getJob().equals(Job.President)) {
			return true;
		}
		if (StaticValue.userLog.getJob().equals(Job.Manager)) {
			String sql = "select empno from assignments where projid = "
					+ projid;
			try {
				ConnToDB.rs = ConnToDB.st.executeQuery(sql);
				while (ConnToDB.rs.next()) {
					if (StaticValue.userLog.getEmpno() == ConnToDB.rs.getInt(1)) {
						return true;
					}
				}
			} catch (SQLException ex) {

			}
		}
		return false;
	}

	public boolean isMgrDept(){ //check mgr of dept or president
		return false;
	}
	
	public boolean isDir(int mgr) { //user mgr
		if (StaticValue.userLog.getJob().equals(Job.President)
				|| StaticValue.userLog.getJob().equals(Job.Manager)
				|| StaticValue.userLog.getEmpno() == mgr) {
			return true;
		} else {
			return false;
		}
	}
}
