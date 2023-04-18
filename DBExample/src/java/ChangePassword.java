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
    private PreparedStatement updateStatement, getStatement;
    
    
    
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
        //String name = request.getParameter("name");
        String username = request.getParameter("username");
        String oldpassword = request.getParameter("oldpassword");
        String newpassword = request.getParameter("newpassword");
        String cpassword = request.getParameter("cpassword");
        
        try{
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
            
            String currentPassword = getCurrentPassword(username);
            if (currentPassword == null || !currentPassword.equals(oldpassword)) {
                out.println("<p>The old password does not match the current one.</p>");
                out.println("<a href='ChangePassword.html'><input type='button' value='Back'></a>");
                return;
            }
        
            storePassword(username, cpassword); 
            out.println("Hello, " + username + ", your password has been updated..");
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
            updateStatement = conn.prepareStatement("Update Account"
                    + " Set password = ?"
                    + " Where username = ?");
            
            getStatement = conn.prepareStatement("Select password"
                    + " From Account"
                    + " Where username = ?");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
    /**
     * Store new record password to the db
     */
    private void storePassword(String username, String cpassword) throws SQLException {
    //private void storePassword(String username, String cpassword) throws SQLException {
        updateStatement.setString(1,cpassword);
        updateStatement.setString(2,username);

        updateStatement.executeUpdate();
    }
    
    private String getCurrentPassword(String username) throws SQLException {
        
        getStatement.setString(1, username);
        ResultSet rs = getStatement.executeQuery();
        
        if (rs.next()) {
            return rs.getString(1);
        }
        
        return null;
    }
}
