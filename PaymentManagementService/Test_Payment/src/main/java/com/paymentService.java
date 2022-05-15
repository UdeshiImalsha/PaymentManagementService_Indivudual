package com;

//For REST Service
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import model.payment;



@Path("/Payment")
public class paymentService {
	
	payment methodObj = new payment();

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public String readItems() {
		return methodObj.allPayment();
	}
	
	@GET
	@Path("/{account}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_HTML)
	public String searchMethod(@PathParam("account") String account) {
		// Convert the input string to an XML document
		//Document doc = Jsoup.parse(methodData, "", Parser.xmlParser());
		// Read the value from the element <itemID>
		//String ID = doc.select("ID").text();
		String output = methodObj.getPaymentHistory(account);
		return output;
	}
	
	@GET
	@Path("bills/{account}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_HTML)
	public String viewBills(@PathParam("account") String account) {
		// Convert the input string to an XML document
		//Document doc = Jsoup.parse(methodData, "", Parser.xmlParser());
		// Read the value from the element <itemID>
		//String ID = doc.select("ID").text();
		String output = methodObj.getAllBills(account);
		return output;
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String insertMethod(@FormParam("bill_no") String bill_no, @FormParam("name") String name, @FormParam("amount_paying") String amount_paying) {
		String output = methodObj.insertMethod(bill_no, name, amount_paying);
		return output;
	}

}
