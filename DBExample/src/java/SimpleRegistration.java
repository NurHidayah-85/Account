/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 *
 * @author LENOVO
 */

public class SimpleRegistration extends HttpServlet {
    
    //Use a prepared statement to store a student into the database
    private PreparedStatement pstmt;
    
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
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String mi = request.getParameter("mi");
        String phone = request.getParameter("telephone");
        String email = request.getParameter("email");
        String address = request.getParameter("street");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String postcode = request.getParameter("postcode");
        
        try{
            if(lastName.length() == 0 || firstName.length() == 0){
                out.println("Last Name and First Name are required");
                return; //End of method
            }
        storeStudent(lastName, firstName, mi, phone, email, address, city, state, postcode);
        out.println(firstName + " " + lastName + " is now registered in the database");
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
            String connectionString = "jdbc:derby://localhost:1527/AddressDB;create=true;user=app;password=app";
            
            //load the driver
            Class.forName(driver);
            
            //connect to the sampple db
            Connection conn = DriverManager.getConnection(connectionString);
            
            //create a statement
            pstmt = conn.prepareStatement("insert into Address" 
                    + "(lastName, firstName, mi, telephone, email, street, city," 
                    + "state, postcode) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
/**
 * Store a student record to the db
 */
private void storeStudent(String lastName, String firstName, String mi, String phone, String email, String address, String city, String state, String postcode) throws SQLException {

pstmt.setString(1, lastName);
pstmt.setString(2, firstName);
pstmt.setString(3, mi);
pstmt.setString(4, phone);
pstmt.setString(5, email);
pstmt.setString(6, address);
pstmt.setString(7, city);
pstmt.setString(8, state);
pstmt.setString(9, postcode);
pstmt.executeUpdate();
}


}
