<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.paymentMethod"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Payment Methods Management</title>
<link rel="stylesheet" href="Views/bootstrap.min.css">
<script src="Components/jquery-3.6.0.min.js"></script>
<script src="Components/paymentmethod.js"></script>
</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-6">
				<hr style="border-width: 5px; border-color: #00008B;">
				<h2>Payment Methods Management</h2>
				<hr style="border-width: 5px; border-color: #00008B;">

				<form id="formMethod" name="formMethod" method="post" action="PaymentMethod.jsp">
					<br> 
					Account: <input id="account" name="account"
						type="text" class="form-control form-control-sm"> <br>
					Account Type: <input id="type" name="type"
						type="text" class="form-control form-control-sm"> <br>
					Card Number: <input id="number" name="number" type="number"
						class="form-control form-control-sm"> <br>
					Expiration Date: <input id="date" name="date"
						type="text" class="form-control form-control-sm"> <br>
					CVV: <input id="cvv" name="cvv" type="text"
						class="form-control form-control-sm"> <br> 
					Account Holder: <input id="name" name="name" type="text" 
						class="form-control form-control-sm"><br>
					Name on Card: <input id="cardName" name="cardName" type="text" 
						class="form-control form-control-sm"><br>
					<input
						id="btnSave" name="btnSave" type="button" value="Save"
						class="btn btn-primary"> <input type="hidden"
						id="hidItemIDSave" name="hidItemIDSave" value=""> <br>
				</form>
				<br>
				<div id="alertSuccess" class="alert alert-success"></div>
				<div id="alertError" class="alert alert-danger"></div>
				<br>
				<div id="divItemsGrid">

					<div>
						<hr style="border-width: 8px; border-color: black;">
						<h1>Previously Added Payment Methods</h1>
						<hr style="border-width: 8px; border-color: black;">

						<br>

					</div>
					<%
					paymentMethod methodObj = new paymentMethod();
					out.print(methodObj.allPaymentMethods());
					%>
				</div>
			</div>
		</div>
	</div>

</body>
</html>