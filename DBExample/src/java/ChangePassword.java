/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 *
 * @author LENOVO
 */

public class ChangePassword extends HttpServlet {
    
    //Use a prepared statement to store a student into the database
    private PreparedStatement pstmt;
    private Statement stmt;
    
    
    
    public void init() throws ServletException {
        initializeJdbc();
        
    }

   
    

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        //Obtain parameters from the client
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String oldpassword = request.getParameter("oldpassword");
        String newpassword = request.getParameter("newpassword");
        String cpassword = request.getParameter("cpassword");
        
        

        
        try{
            
            
            
             ResultSet rs = stmt.executeQuery("select username, password from Account");  
             
             //out.println("<table border=1 width=50% height=50%>");  
             //out.println("<tr><th>Username</th><th>Password</th><th>Name</th><tr>");  
             while (rs.next()) 
             {  
                 String user = rs.getString("username");  
                 String pass = rs.getString("password");
                 String uname = rs.getString("name");
                 
                 if (user != username) {
                    if ( pass != oldpassword) {
                        System.out.println("data is not in system!");
                    }
                    else{
                        System.out.println("Recorded!");
                        
                    }
}

                 //out.println("<tr><td>" + user + "</td><td>" + pass + "</td><td>" + nm + "</td></tr>");   
             }  
            
             /*ResultSet rs = pstmt.executeQuery("select username,password from Account");  
             //out.println("<table border=1 width=50% height=50%>");  
             //out.println("<tr><th>Username</th><th>Password</th><th>Name</th><tr>");  
             while (rs.next()) 
             {  
                                
                  username = rs.getString("username");  
                  oldpassword = rs.getString("password");  
                   name = rs.getString("name");
             
 
                if (username != rs.getString("username")&& oldpassword != rs.getString("password")) {
                        System.out.println("data bot recorded!");
                    }*/
                 
                 /*username = rs.getString("username");  
                  oldpassword = rs.getString("password");  
                 name = rs.getString("name");  */

                 //out.println("<tr><td>" + user + "</td><td>" + pass + "</td><td>" + nm + "</td></tr>");   
             
//error message occur if username and existing password is not in Account table
            //error message occur if not match
            /*if(!username.equals(username) && oldpassword.equals(password) ){
                out.println("New password and confirm password are <font color=\"#FF0000\">not match</font>");
                out.println("<a href=ChangePassword.html>Back</a>");//back to previous page
                return; //End of method
            }*/
            
          if(!username.equals(username)|| !oldpassword.equals(oldpassword))
            {
                out.println("No data registered");
            }

            //error message occur if username and old password is not valid
            if(username.length() == 0 || oldpassword.length() == 0){
                out.println("Username and Old Password are <font color=\"#FF0000\">required</font>");
                out.println("<a href=ChangePassword.html>Back</a>"); //back to previous page
                return; //End of method
            }
            
            //error message occur if not match
            if(!newpassword.equals(cpassword)){
                out.println("New password and confirm password are <font color=\"#FF0000\">not match</font>");
                out.println("<a href=ChangePassword.html>Back</a>");//back to previous page
                return; //End of method
            }
            
        
        storePassword(username, cpassword, name); 
        out.println("Hello, " + rs.getString("name") + ", your password has been updated..");
        } catch (Exception ex){
            out.println("Error: " + ex.getMessage());
        } finally {
            out.close(); //Close stream
        }
    
    }

    /**
     * Initialize db connection
     */
    private void initializeJdbc(){
        try{
            //declare driver and connection string
            String driver = "org.apache.derby.jdbc.ClientDriver";
            String connectionString = "jdbc:derby://localhost:1527/ChangePasswordDB;create=true;user=app;password=app";
            
           
            
            //load the driver
            Class.forName(driver);
            
            //connect to the sampple db
            Connection conn = DriverManager.getConnection(connectionString);
            
            //create a statement
           
             pstmt = conn.prepareStatement("UPDATE Account SET  password = ? WHERE username = ?");
             stmt = conn.createStatement();  
            
             
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
 
    
 
/**
 * Store new record password to the db
 */
private void storePassword(String username, String cpassword, String name) throws SQLException {
//private void storePassword(String username, String cpassword) throws SQLException {
pstmt.setString(1,username );
pstmt.setString(2,cpassword );
pstmt.setString(3, name);

pstmt.executeUpdate();
}


}
