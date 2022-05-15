package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class payment {
	
	// A common method to connect to the DB
		public Connection connect() {

			Connection con = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/payment", "root", "IMAL1999SHA@");
				// For testing
				System.out.print("Successfully connected");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return con;
		}

		//checked
		public String insertMethod(String bill_no, String name, String amount_paying) {

			String output = "";
			String account = "";
			Double bill_due = 0.0;

			try {
				Connection con = connect();

				if (con == null) {
					return "Error while connecting to the database";
				}

				Statement stmt = con.createStatement();
				String query2 = "select * from bill where billNo='" + bill_no + "'";
				ResultSet rs = stmt.executeQuery(query2);

				while (rs.next()) {
					account = rs.getString("accountNo");
					bill_due = rs.getDouble("due");
				}

				// create a prepared statement
				String query = "insert into payments(`ID`,`account`,`bill_no`,`name`,`amount_required`,`amount_paying`,`amount_due`,`date`)"
						+ " values (?, ?, ?, ?, ?, ?, ?, now())";
				PreparedStatement preparedStmt = con.prepareStatement(query);

				Double payAmount = Double.parseDouble(amount_paying);

				Double pay_due = bill_due - payAmount;

				// binding values
				preparedStmt.setInt(1, 0);
				preparedStmt.setString(2, account);
				preparedStmt.setString(3, bill_no);
				preparedStmt.setString(4, name);
				preparedStmt.setDouble(5, bill_due);
				preparedStmt.setDouble(6, payAmount);
				preparedStmt.setDouble(7, pay_due);
				// preparedStmt.setString(8, date);

				// execute the statement
				preparedStmt.execute();
				con.close();

				String result = updateMethod(bill_no, pay_due);
				String show = getBill(bill_no);

				output = "Inserted Successfully! " + result + show;
			} catch (Exception e) {
				output = "Error while inserting!";
				System.err.println(e.getMessage());
			}

			return output;

		}

		//get all payment history of a PARTICULAR user
		//checked
		public String getPaymentHistory(String account) {

			String output = "";

			try {
				Connection con = connect();

				if (con == null) {
					return "Error while connecting to the database for reading";
				}

				// check from here..................................
				// Prepare the html table to be displayed
				output = "<table class='table' border='1'><tr><th scope='col'>Bill No</th>" + "<th scope='col'>Amount Paid</th><th scope='col'>Date & Time</th>"
						+ "<th scope='col'>Name</th>";

				Statement stmt = con.createStatement();
				String query = "select * from payments where account='" + account + "'";
				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {

					//String ID = Integer.toString(rs.getInt("ID"));
					String bill_no = rs.getString("bill_no");
					String amount_paying = rs.getString("amount_paying");
					String date = rs.getString("date");
					String name = rs.getString("name");

					// Add new row to the html table
					output += "<tr><td>" + bill_no + "</td>";
					output += "<td>" + amount_paying + "</td>";
					output += "<td>" + date + "</td>";
					output += "<td>" + name + "</td></tr>";

				}

				con.close();

				// Complete the html table
				output += "</table>";

			} catch (Exception e) {
				output = "Error while retrieving!";
				System.err.println(e.getMessage());
			}

			return output;

		}
		
		//get all the bills of a PARTICULAR user
		//checked
		public String getAllBills(String account) {

			String output = "";

			try {
				Connection con = connect();

				if (con == null) {
					return "Error while connecting to the database for reading";
				}

				// check from here..................................
				// Prepare the html table to be displayed
				output = "<table class='table' border='1'><tr><th scope='col'>Bill No</th>" + "<th scope='col'>Account No</th><th scope='col'>Total Amount</th>"
						+"<th scope='col'>Due Amount</th><th scope='col'>Status</th>"+"<th scope='col'>Pay Bill</th></tr>";

				Statement stmt = con.createStatement();
				String query = "select * from bill where accountNo='" + account + "'";
				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {

					String bill_no = Integer.toString(rs.getInt("billNo"));
					String user_account = rs.getString("accountNo");
					String total = rs.getString("total");
					String due = rs.getString("due");
					String status = rs.getString("status");

					// Add new row to the html table
					output += "<tr><td>" + bill_no + "</td>";
					output += "<td>" + user_account + "</td>";
					output += "<td>" + total + "</td>";
					output += "<td>" + due + "</td>";
					output += "<td>" + status + "</td>";

					// buttons
					output += "<td><form method='post' action='insertPaymentDetails.jsp'>" 
							+"<input name='btnPay' type='submit' value='Pay' class='btn btn-success'>"
							+ "<input name='billNo' type='hidden' value='"+ bill_no + "'>" + "</form></td></tr>";
				}

				con.close();

				// Complete the html table
				output += "</table>";

			} catch (Exception e) {
				output = "Error while retrieving!";
				System.err.println(e.getMessage());
			}

			return output;

		}
		
		//insert payment details
		public String getBill (String billNo) {
			
			String output = "";
			
			
			try {
				Connection con = connect();

				if (con == null) {
					return "Error while connecting to the database for reading";
				}

				
				Statement stmt = con.createStatement();
				String query = "select * from bill where billNo='"+billNo+"'";
				ResultSet rs = stmt.executeQuery(query);
				
					while(rs.next()) {
						
						String bill_no = Integer.toString(rs.getInt("billNo"));
						String account = rs.getString("accountNo");
						String total = rs.getString("total");
						String due = rs.getString("due");
	 
						output = "<form method='post' action='makePayment.jsp'>"
								+"<label>Bill No : </label><span>"
								+ "<input name='billNo' type='text' value='" + bill_no + "' class='form-control' readonly><br>"
								+"<label>User Account : </label><span>"
								+ "<input name='account' type='text' value='" + account + "' class='form-control' readonly><br>"
								+"<label>Total Monthly Amount : </label><span>"
								+ "<input name='total' type='text' value='" + total + "' class='form-control' readonly><br>"
								+"<label>Amount Due : </label><span>"
								+ "<input name='due' type='text' value='" + due + "' class='form-control' readonly><br>"
								+"<label>User Name : </label><span>"
								+ "<input name='name' type='text' value='' class='form-control'><br>"
								+"<label>Amount Paying : </label><span>"
								+ "<input name='amountPay' type='text' value='' class='form-control'><br>"
								+ "<input name='btnSubmit' type='submit' value='Pay' class='btn btn-success'>";
								
					}
					
					con.close();

					// Complete the html table
					output += "</form>";
				
			} catch (Exception e) {
				output = "Error while searching!";
				System.err.println(e.getMessage());
			}

			return output;
			
		}


		// All payment details for all users --For ADMIN
		//checked
		public String allPayment() {
			String output = "";

			try {
				Connection con = connect();

				if (con == null) {
					return "Error while connecting to the database for reading";
				}

				// check from here..................................
				// Prepare the html table to be displayed
				output = "<table class='table' border='1'><tr><th scope='col'>Bill No</th>" + "<th scope='col'>Amount Paid</th><th scope='col'>Date & Time</th>"
						+ "<th scope='col'>Name</th></tr>";

				String query = "select * from payments";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);

				// `ID`,`paymentType`,`cardNumber`,`expDate`,`cvv`,`cardHolderName`,`nameOnCard`
				// iterate through the rows in the result set
				while (rs.next()) {
					//String ID = Integer.toString(rs.getInt("ID"));
					String bill_no = rs.getString("bill_no");
					String amount_paying = rs.getString("amount_paying");
					// String expDate = rs.getString("expDate");
					// String cvv = rs.getString("cvv");
					String date = rs.getString("date");
					String name = rs.getString("name");

					// Add new row to the html table
					output += "<tr><td>" + bill_no + "</td>";
					output += "<td>" + amount_paying + "</td>";
					output += "<td>" + date + "</td>";
					output += "<td>" + name + "</td></tr>";

				}

				con.close();

				// Complete the html table
				output += "</table>";
			} catch (Exception e) {
				output = "Error while reading!";
				System.err.println(e.getMessage());
			}

			return output;
		}

		// update to bill table for due amount and status after paying
		//checked
		public String updateMethod(String bill_no, Double due) {
			String output = "";
			String status = "";

			if (0 < due) {
				status = "half paid";
			} else {
				status = "Fully paid";
			}

			try {
				Connection con = connect();

				if (con == null) {
					return "Error while connecting to the database for updating.";
				}

				// create a prepared statement
				// `ID`,`paymentType`,`cardNumber`,`expDate`,`cvv`,`cardHolderName`,`nameOnCard`
				// UPDATE payment.bills SET due=1000.00,status="Half Paid" WHERE bill_no=4;
				String query = "UPDATE bill SET due=?,status=? WHERE billNo=?";
				PreparedStatement preparedStmt = con.prepareStatement(query);

				// binding values
				preparedStmt.setDouble(1, due);
				preparedStmt.setString(2, status);
				preparedStmt.setString(3, bill_no);

				// execute the statement
				preparedStmt.execute();
				con.close();
				output = "Updated successfully";
			} catch (Exception e) {
				output = "Error while updating the payment method.";
				System.err.println(e.getMessage());
			}
			return output;
		}


}
