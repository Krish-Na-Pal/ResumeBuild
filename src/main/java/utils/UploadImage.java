package utils;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UploadImage
 */
@WebServlet("/UploadImage")
public class UploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
        
        //set profile Photo
        FileInputStream profilepic = new FileInputStream(request.getParameter("profilepic"));
        String username = request.getParameter("username");
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            String user = "root";
            String pass = "123456";
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", user, pass);
            stmt = conn.prepareStatement("update ownertable set profilepic=? where username=?");
            stmt.setBinaryStream(1, profilepic);
            stmt.setString(2, username);

            int rowsInserted = stmt.executeUpdate();
            if(rowsInserted>0) {
            	response.sendRedirect("ResumeBuilder.html");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UploadImage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UploadImage.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}
