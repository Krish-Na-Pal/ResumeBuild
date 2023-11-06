package resume;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import utils.AlertMessage;
import utils.UserAuthenticat;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
        
        //get all the data from request
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //System.out.print("\n"+username+password+"\n");
        try{
            String ownerId = UserAuthenticat.authenticat("ownertable", password, username,"ownerId");
            if(ownerId != ""){
                Cookie venuezarCookie = new Cookie("venuezar_id", ownerId);
                response.addCookie(venuezarCookie);
                response.sendRedirect("ResumeBuilder.html");
            }
            else{
                String res = AlertMessage.alertMessage("Ivalid Username and Passowrd....... Please try again.","Register.html");
                out.print(res);
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
	}

}
