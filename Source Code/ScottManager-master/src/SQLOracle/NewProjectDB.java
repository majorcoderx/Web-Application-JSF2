package SQLOracle;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import Models.Project;

public class NewProjectDB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sql;
	PreparedStatement pr = null;
	
	public String createNewProject(Project proj){
		System.out.println("MA PROJECT: "+proj.getProjId());
		String msg = "";
		sql = "insert into project(projid,p_desc,p_start_date,p_end_date,budget_amount,max_no_staff,comments) "
				+ "values(?,?,?,?,?,?,?)";
		try{
			java.sql.Date sDate = new java.sql.Date(proj.getStartDate().getTime());
			java.sql.Date edate = new java.sql.Date(proj.getEndDate().getTime());
			pr = ConnToDB.conn.prepareStatement(sql);
			pr.setInt(1, proj.getProjId());
			pr.setString(2, proj.getDescrible());
			pr.setDate(3, sDate);
			pr.setDate(4, edate);
			pr.setInt(5, proj.getBudgetAmount());
			pr.setInt(6, proj.getMaxStaff());
			pr.setInt(7, proj.getComment());
			int udate = pr.executeUpdate();
			if(udate > 0){
				msg = "Create new Project success";
			}
		}catch(SQLException ex){
			ex.printStackTrace();
			msg = "Create new fail";
		}
		return msg;
	}
	
	public List<Integer> getProjidInDB(){
		sql = "select projid from project";
		List<Integer> lI = new LinkedList<Integer>();
		try{
			ConnToDB.rs = ConnToDB.st.executeQuery(sql);
			while(ConnToDB.rs.next()){
				lI.add(ConnToDB.rs.getInt(1));
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return lI;
	}
	
	public Project randomProject(){
		Random random = new Random();
		boolean check = true;
		int iDR = 0;

		List<Integer> plist = new LinkedList<Integer>(getProjidInDB());
		while(check){
			check = false;
			iDR = random.nextInt(9999) + 1;
			for(int  i = 0;i<plist.size();++i){
				if(plist.get(i) == iDR){
					check = true;
					break;
				}
			}
		}
		Project proj = new Project();
		proj.setProjId(iDR);
		proj.setDescrible("");
		Date date = new Date(113, 12, 12);
		java.sql.Date jdate = new java.sql.Date(date.getTime());
		proj.setStartDate(jdate);
		date = new Date();
		jdate = new java.sql.Date(date.getTime());
		proj.setEndDate(jdate);
		proj.setBudgetAmount(0);
		proj.setMaxStaff(0);
		proj.setComment(0);
		return proj;
	}
}
