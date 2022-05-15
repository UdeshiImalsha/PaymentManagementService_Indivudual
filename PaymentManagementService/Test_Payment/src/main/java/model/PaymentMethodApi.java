package model;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PaymentMethodApi
 */
@WebServlet("/PaymentMethodApi")
public class PaymentMethodApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	paymentMethod methodObj = new paymentMethod();
	
	private static Map getParasMap(HttpServletRequest request) 
	{ 
		 Map<String, String> map = new HashMap<String, String>(); 
		try
		 { 
			 Scanner scanner = new Scanner(request.getInputStream(), "UTF-8"); 
			 String queryString = scanner.hasNext() ? 
			 scanner.useDelimiter("\\A").next() : ""; 
			 scanner.close(); 
			 String[] params = queryString.split("&"); 
				 for (String param : params) 
				 { 
					 String[] p = param.split("=");
					 map.put(p[0], p[1]); 
				 } 
		} 
		catch (Exception e) 
		{ 
		 } 
			return map; 
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentMethodApi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String output = methodObj.insertMethod(request.getParameter("account"), 
				request.getParameter("type"),
				request.getParameter("number"), 
				request.getParameter("date"), 
				request.getParameter("cvv"),
				request.getParameter("name"), 
				request.getParameter("cardName"));

		response.getWriter().write(output);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map paras = getParasMap(request); 
		String output = methodObj.updateMethod(paras.get("hidItemIDSave").toString(), 
											 paras.get("date").toString(), 
											 paras.get("cvv").toString()); 
		response.getWriter().write(output);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map paras = getParasMap(request); 
		String output = methodObj.deleteMethod(paras.get("ID").toString()); 
		response.getWriter().write(output);
	}
	
	

}
