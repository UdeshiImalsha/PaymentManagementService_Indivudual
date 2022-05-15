$(document).ready(function() {
	if ($("#alertSuccess").text().trim() == "") 
	{
		$("#alertSuccess").hide();
	}
	$("#alertError").hide();
});

// SAVE ============================================
$(document).on("click", "#btnSave", function(event) {
	
	// Clear alerts---------------------
	$("#alertSuccess").text("");
	$("#alertSuccess").hide();
	$("#alertError").text("");
	$("#alertError").hide();
	
	// Form validation-------------------
	var status = validatePaymentMethodForm();
	if (status != true) {
		$("#alertError").text(status);
		$("#alertError").show();
		return;
	}
	
	// If valid------------------------
	var type = ($("#hidItemIDSave").val() == "") ? "POST" : "PUT"; 
	
		 $.ajax( 
		 { 
			 url : "PaymentMethodApi", 
			 type : type, 
			 data : $("#formMethod").serialize(), 
			 dataType : "text", 
			 complete : function(response, status) 
		 { 
			 onItemSaveComplete(response.responseText, status); 
		 } 
	 });
});

// UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event) {
	$("#hidItemIDSave").val($(this).data("itemid")); 
	$("#account").val($(this).closest("tr").find('td:eq(0)').text());
	$("#type").val($(this).closest("tr").find('td:eq(2)').text());
	$("#number").val($(this).closest("tr").find('td:eq(4)').text());
	$("#date").val($(this).closest("tr").find('td:eq(5)').text());
	$("#cvv").val($(this).closest("tr").find('td:eq(6)').text());
	$("#name").val($(this).closest("tr").find('td:eq(1)').text());
	$("#cardName").val($(this).closest("tr").find('td:eq(3)').text());
});

//DELETE ======================
$(document).on("click", ".btnRemove", function(event)
{ 
	 $.ajax( 
		 { 
			 url : "PaymentMethodApi", 
			 type : "DELETE", 
			 data : "ID=" + $(this).data("itemid"),
			 dataType : "text", 
			 complete : function(response, status) 
			{ 
				 onItemDeleteComplete(response.responseText, status); 
		 } 
	 }); 
});

// CLIENT-MODEL================================================================
function validatePaymentMethodForm() {
	// ACCOUNT
	if ($("#account").val().trim() == "") {
		return "Insert Account Number.";
	}
	// TYPE
	if ($("#type").val().trim() == "") {
		return "Insert Account Type.";
	}
	// NUMBER-------------------------------
	if ($("#number").val().trim() == "") {
		return "Insert Card Number.";
	}
	
	// is numerical value
	//var tmpPrice = $("#itemPrice").val().trim();
	//if (!$.isNumeric(tmpPrice)) {
	//	return "Insert a numerical value for Item Price.";
	//}
	// convert to decimal price
	//$("#itemPrice").val(parseFloat(tmpPrice).toFixed(2));
	
	// EXP DATE------------------------
	if ($("#date").val().trim() == "") {
		return "Insert Expiration Date.";
	}
	
	// CVV NUMBER------------------------
	if ($("#cvv").val().trim() == "") {
		return "Insert CVV Number.";
	}
	
	// CARD HOLDER------------------------
	if ($("#name").val().trim() == "") {
		return "Insert Card Holder Name.";
	}
	
	// CARD NAME------------------------
	if ($("#cardName").val().trim() == "") {
		return "Insert Name On Card.";
	}
	
	return true;
}

function onItemSaveComplete(response, status)
{ 
	if (status == "success") 
	 { 
		 var resultSet = JSON.parse(response); 
		 if (resultSet.status.trim() == "success") 
	 { 
		 $("#alertSuccess").text("Successfully saved."); 
		 $("#alertSuccess").show(); 
		 $("#divItemsGrid").html(resultSet.data); 
	 }
		 else if (resultSet.status.trim() == "error") 
	 { 
		 $("#alertError").text(resultSet.data); 
		 $("#alertError").show(); 
	 } 
	 }
		else if (status == "error") 
	 { 
		 $("#alertError").text("Error while saving."); 
		 $("#alertError").show(); 
	 } else
	 { 
		 $("#alertError").text("Unknown error while saving.."); 
		 $("#alertError").show(); 
	 }
		 $("#hidItemIDSave").val(""); 
		 $("#formMethod")[0].reset(); 
}

function onItemDeleteComplete(response, status)
{ 
	if (status == "success") 
	 { 
		 var resultSet = JSON.parse(response); 
		 if (resultSet.status.trim() == "success") 
	 { 
		 $("#alertSuccess").text("Successfully deleted."); 
		 $("#alertSuccess").show(); 
		 $("#divItemsGrid").html(resultSet.data); 
	 } 
		 else if (resultSet.status.trim() == "error") 
	 { 
		 $("#alertError").text(resultSet.data); 
		 $("#alertError").show(); 
	 } 
	 } 
		 else if (status == "error") 
	 { 
		 $("#alertError").text("Error while deleting."); 
		 $("#alertError").show(); 
	 } else
	 { 
		 $("#alertError").text("Unknown error while deleting.."); 
		 $("#alertError").show(); 
	 } 
}