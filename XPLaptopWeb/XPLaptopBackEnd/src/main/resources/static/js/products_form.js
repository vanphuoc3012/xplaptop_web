$(document).ready(function() {
	$("#shortDescription").richText({maxlength: 500});
	$("#fullDescription").richText({maxlength: 4096});
	
	
	// cancle button
	$("#buttonCancel").on("click", function() {
		window.location = moduleURL;
	});
			
	/* check unique of name and alias when click submit button */
	
	$("#productForm").on("submit", checkUnique);
			
	/* autofill alias */
	$("#productName").on("keyup", function() {
		productName = $("#productName").val();			
		$("#productAlias").val(productName.toLowerCase().replaceAll(" ", "_"));
	});
			
	/* load category after choose brand 
	send post request to Restful Controller and receive list categories
	*/
	$("#brand").on("change", function() {
		$("#category").empty();
		if($(this).val() == 0) {
			$("<option>").val(null).text('No category').appendTo($("#category"));
		}
		
		bId = $(this).val();
		URL = getcategoriesURL;			 
		csrfValue = $("input[name='_csrf']").val();
		
		body = {_csrf: csrfValue, brandId: bId};
		$.post(URL, body, function(data) {
			$.each(data, function(i, category) {
				$("<option>").val(category.id).text(category.name).appendTo($("#category"));
			})
		});
	});
	
});

function showImgThumbnail(fileInput) {
	
	const file = fileInput.files[0];
	let reader = new FileReader();
	reader.onload = function(event) {
		$(fileInput).prev().prev().prev().attr("src", event.target.result);
	}
	reader.readAsDataURL(file);
};

function checkUnique(event) {
	event.preventDefault();
	url = checkUniqueUrl;
	entityId = $("#id").val();
	entityName = $("#productName").val();
	entityAlias = $("#productAlias").val();
	csrfValue = $("input[name='_csrf']").val();			
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$(document).ajaxSend(function(e, xhr, options) {
        if (options.type == "POST") {
            xhr.setRequestHeader(header, token);
        }
    });
	
	$.ajax({
	    url: url,
	    dataType: 'text',
	    type: 'post',
	    contentType: 'application/json',
	    data: JSON.stringify({
							 "id": entityId,
							 "name": entityName									 
							 }),
	    processData: false,
	    success: function(data){
	        if(data === "OK") {
				$("#productForm").off("submit");
				$("#productForm").submit();
			} else {
				if(data === "DuplicatedName") {
					showModalDialog("Duplicated Name", `Please choose another name`);
				} else if(data === "DuplicatedAlias") {
					showModalDialog("Duplicated Alias", `Please choose another alias`);
				}
				
			}
	    },
	    error: function(xhr,status,error){
	    	showModalDialog("Error", `Could not connect to the server`);
	    }
	});
	
	
};
		
function checkNumber(inputElement) {
	if(inputElement.val() <= 0.0) {					
		inputElement.get(0).setCustomValidity('Must be greater than 0');
	} else {
		inputElement.setCustomValidity('');
	}
};

function checkLengthDescription(inputElement) {
	if(inputElement.val().split(" ").length <= 50) {					
		inputElement.get(0).setCustomValidity('Enter at least 50 words');
	} else {
		inputElement.setCustomValidity('');
	}
};
		
function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal("show");
};