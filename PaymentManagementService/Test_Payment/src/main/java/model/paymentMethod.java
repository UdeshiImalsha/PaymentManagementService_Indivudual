package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class paymentMethod {
	
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

			//INSERT a payment method
			public String insertMethod(String account, String type, String number, String date, String cvv, String name, String cardName) {

				String output = "";
				String ID="";
				
				try {
					Connection con = connect();

					if (con == null) {
						return "Error while connecting to the database";
					}

					// create a prepared statement
					String query = "insert into paymentMethod(`ID`,`EaccontNo`,`cardHolderName`,`paymentType`,`nameOnCard`,`cardNumber`,`expDate`,`cvv`)"
							+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
					
					
					PreparedStatement preparedStmt = con.prepareStatement(query);

					// binding values
					preparedStmt.setInt(1, 0);
					preparedStmt.setString(2, account);
					preparedStmt.setString(3, name);
					preparedStmt.setString(4, type);
					preparedStmt.setString(5, cardName);
					preparedStmt.setString(6, number);
					preparedStmt.setString(7, date);
					preparedStmt.setString(8, cvv);

					// execute the statement
					preparedStmt.execute();
					con.close();

					String newPaymentMethod = allPaymentMethods(); 
					output = "{\"status\":\"success\", \"data\": \"" + newPaymentMethod +"\"}";
					 
				} catch (Exception e) {
					output = "{\"status\":\"error\", \"data\": \"Error while inserting the payment methods.\"}"; 
					System.err.println(e.getMessage());
				}

				return output;

			}
			
			
			//to show payment methods for one user
			public String searchMethod (String accont) {
				
				String output = "";
				
				
				try {
					Connection con = connect();

					if (con == null) {
						return "Error while connecting to the database for reading";
					}
					
					//check from here..................................
					// Prepare the html table to be displayed
					output = "<table class='table' border='1'><tr><th scope='col'>Payment Type</th>" + "<th scope='col'>Name on Card</th scope='col'><th scope='col'>Card Number</th>"
							+ "<th scope='col'>Expiration Date</th>" + "<th scope='col'>Update</th><th scope='col'>Remove</th></tr>";

					
					Statement stmt = con.createStatement();
					String query = "select * from paymentMethod where EaccontNo='"+accont+"'";
					ResultSet rs = stmt.executeQuery(query);
					
						while(rs.next()) {
							
							String ID = Integer.toString(rs.getInt("ID"));
							String paymentType = rs.getString("paymentType");
							String cardNumber = rs.getString("cardNumber");
							String expDate = rs.getString("expDate");
							String nameOnCard = rs.getString("nameOnCard");
							

							String updatedCardNo = cardNumber.substring(0,6)+"******"+cardNumber.substring(12,16);
		 
							// Add new row to the html table
							output += "<tr><td>" + paymentType + "</td>";
							output += "<td>" + nameOnCard + "</td>";
							output += "<td>" + updatedCardNo + "</td>";
							output += "<td>" + expDate + "</td>";

							// buttons
							output += "<td><form method='post' action='updatePaymentMethod.jsp'>"
									+"<input name='btnUpdate' type='submit' value='Update' class='btn btn-secondary'>"
									+ "<input name='ID' type='hidden' value='" + ID + "'>"
									+ "</form></td>"
									+"<td><form method='post' action='deletePaymentMethod.jsp'>"
									+ "<input name='btnRemove' type='submit' value='Remove' class='btn btn-danger'>"
									+ "<input name='ID' type='hidden' value='" + ID + "'>"
									+ "<input name='account' type='hidden' value='" + accont + "'>"
									+ "</form></td></tr>";
						}
						
						con.close();

						// Complete the html table
						output += "</table>";
					
				} catch (Exception e) {
					output = "Error while searching!";
					System.err.println(e.getMessage());
				}

				return output;
				
			}
			
			//to show one particular payment method
			public String getPaymentMethod (String ID) {
				
				String output = "";
				
				
				try {
					Connection con = connect();

					if (con == null) {
						return "Error while connecting to the database for reading";
					}

					
					Statement stmt = con.createStatement();
					String query = "select * from paymentMethod where ID='"+ID+"'";
					ResultSet rs = stmt.executeQuery(query);
					
						while(rs.next()) {
							
							String id = Integer.toString(rs.getInt("ID"));
							String account = rs.getString("EaccontNo");
							String paymentType = rs.getString("paymentType");
							String cardNumber = rs.getString("cardNumber");
							String cardHolderName = rs.getString("cardHolderName");
							String nameOnCard = rs.getString("nameOnCard");
							String cvv = rs.getString("cvv");
							String exp = rs.getString("expDate");
							

							String updatedCardNo = cardNumber.substring(0,6)+"******"+cardNumber.substring(12,16);
		 
							output = "<form method='post' action='updatePaymentProcess.jsp'>"
									+ "<input name='ID' type='hidden' value='" + id + "' class='form-control'><br>"
									+ "<input name='account' type='hidden' value='" + account + "' class='form-control'><br>"
									+ "<input name='paymentType' type='text' value='" + paymentType + "' class='form-control' readonly><br>"
									+ "<input name='updatedCardNo' type='text' value='" + updatedCardNo + "' class='form-control' readonly><br>"
									+ "<input name='cardHolderName' type='text' value='" + cardHolderName + "' class='form-control' readonly><br>"
									+ "<input name='nameOnCard' type='text' value='" + nameOnCard + "' class='form-control' readonly><br>"
									+ "<input name='cvv' type='text' value='" + cvv + "' class='form-control'><br>"
									+ "<input name='exp' type='text' value='" + exp + "' class='form-control'><br><br>"
									+ "<input name='btnSubmit' type='submit' value='Update' class='btn btn-secondary'>";
									
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
			

			//All payment methods for all the accounts
			public String allPaymentMethods() {
				String output = "";

				try {
					Connection con = connect();

					if (con == null) {
						return "Error while connecting to the database for reading";
					}

					//`ID`,`EaccontNo`,`cardHolderName`,`paymentType`,`nameOnCard`,`cardNumber`,`expDate`,`cvv`
					// Prepare the html table to be displayed
					output = "<table border='1' style='table'><tr><th>Account Number</th><th>Account Holder</th><th>Payment Type</th>" + "<th>Name on Card</th><th>Card Number</th>"
							+ "<th>Expiration Date</th><th>CVV</th>" + "<th>Update</th><th>Remove</th></tr>";

					String query = "select * from paymentMethod";
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);

					// iterate through the rows in the result set
					while (rs.next()) {
						String ID = Integer.toString(rs.getInt("ID"));
						String EaccontNo = rs.getString("EaccontNo");
						String cardHolderName = rs.getString("cardHolderName");
						String paymentType = rs.getString("paymentType");
						String nameOnCard = rs.getString("nameOnCard");
						String cardNumber = rs.getString("cardNumber");
						String expDate = rs.getString("expDate");
						String cvv = rs.getString("cvv");
						

						String updatedCardNo = cardNumber.substring(0,6)+"******"+cardNumber.substring(12,16);
	 
						// Add new row to the html table
						output += "<tr><td><input id='hidPaymentIDUpdate' name='hidPaymentIDUpdate'"
								+ "type='hidden' value='"+ID+"'>"
								+ EaccontNo + "</td>";
						output += "<td>" + cardHolderName + "</td>";
						output += "<td>" + paymentType + "</td>";
						output += "<td>" + nameOnCard + "</td>";
						output += "<td>" + updatedCardNo + "</td>";
						output += "<td>" + expDate + "</td>";
						output += "<td>" + cvv + "</td>";

						// buttons
						output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary' data-itemid='" + ID +"'></td>"
								+"<td>" 
								+ "<input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-itemid='" + ID + "'>"
								+ "</td></tr>";
						
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

			
			//UPDATE exp date or the cvv of a card
			//String type, String number, String date, String cvv, String name, String cardName
			public String updateMethod(String ID, String date, String cvv) {
				String output = "";

				try {
					Connection con = connect();

					if (con == null) {
						return "Error while connecting to the database for updating.";
					}

					// create a prepared statement
					//`ID`,`paymentType`,`cardNumber`,`expDate`,`cvv`,`cardHolderName`,`nameOnCard`
					String query = "UPDATE paymentMethod SET expDate=?,cvv=? WHERE ID=?";
					PreparedStatement preparedStmt = con.prepareStatement(query);
					
					// binding values
					preparedStmt.setString(1, date);
					preparedStmt.setString(2, cvv);
					preparedStmt.setString(3, ID);
					
					// execute the statement
					preparedStmt.execute();
					con.close();
					
					String newPaymentMethod = allPaymentMethods(); 
					output = "{\"status\":\"success\", \"data\": \"" + newPaymentMethod + "\"}";
					 
				} catch (Exception e) {
					output = "{\"status\":\"error\", \"data\": \"Error while updating the payment methods.\"}";
					System.err.println(e.getMessage());
				}
				return output;
			}	
			
			
			//REMOVE a payment method
			public String deleteMethod(String ID) {
				String output = "";
				try {
					Connection con = connect();
					if (con == null) {
						return "Error while connecting to the database for deleting.";
					}
					// create a prepared statement
					String query = "delete from paymentMethod where ID=?";
					PreparedStatement preparedStmt = con.prepareStatement(query);
					// binding values
					preparedStmt.setInt(1, Integer.parseInt(ID));
					// execute the statement
					preparedStmt.execute();
					con.close();
					
					String newPaymentMethod = allPaymentMethods(); 
					output = "{\"status\":\"success\", \"data\": \"" + newPaymentMethod + "\"}";
					
				} catch (Exception e) {
					output = "{\"status\":\"error\", \"data\": \"Error while deleting payment method.\"}";
					System.err.println(e.getMessage());
				}
				return output;
			}

}
