package resume;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.AlertMessage;
import utils.PasswordHasher;


@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
        //generate random id
        String ownerId = UUID.randomUUID().toString().replace("-", "_");;
        
        
        //get all the data from request
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String con_password = request.getParameter("con_password");
        
        if(!password.equals(con_password)){
            response.setContentType("text/html");
            try {
                String res = AlertMessage.alertMessage("The password and confirm password do not match. Do you want to try again?","Register.html");
                out.print(res);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                String hashedPassword = PasswordHasher.hashPassword(password);
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                String user = "root";
                String pass = "123456";
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", user, pass);
                stmt = conn.prepareStatement("INSERT INTO ownertable VALUES (?, ?, ?, ?, ?)");
                stmt.setString(1, ownerId);
                stmt.setString(2, username);
                stmt.setString(3, email);
                stmt.setString(4, hashedPassword);
                stmt.setString(5, null);

                int rowsInserted = stmt.executeUpdate();
                
                if (rowsInserted > 0) {
                    response.sendRedirect("Login.html");
                } else {
                    try {
                        String res = AlertMessage.alertMessage("Registration Of Owner Is Faild....... Please try again.","Register.html");
                        out.print(res);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

	}

}


