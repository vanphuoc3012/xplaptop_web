var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	buttonAddCountry = $("#btnAddCountry");
	buttonUpdateCountry = $("#btnUpdateCountry");
	buttonDeleteCountry = $("#btnDeleteCountry");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");
	
	buttonLoad.click(function() {
		loadCountries();
	});
	
	dropDownCountry.on("change", function() {
		changeFormStateToSelectedCountry();
	});
	
	buttonAddCountry.click(function() {
		if(buttonAddCountry.val() == "Add") {
			addCountry();
		} else {
			changeFormStateToNew();
		}
	});
	
	buttonUpdateCountry.click(function() {
		updateCountry();
	});
	
	buttonDeleteCountry.click(function() {
		deleteCountry();
	})
});

function deleteCountry() {
	countryId = dropDownCountry.val().split("-")[0];
	countryCode = fieldCountryCode.val();
	optionValue = countryId + "-" + countryCode;
	
	url = contextPath + "countries/delete/" + countryId;
	$.get(url, function() { 
		$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true).remove();
	}).done(function() {
		showToastMessage("The country has been deleted");
		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("Error: Could not connect to server or server encounter an error");
	});
}

function updateCountry() {
	if(!validateFormCountry()) return;
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	countryId = dropDownCountry.val().split("-")[0];
	
	jsonData = {id: countryId, name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		showToastMessage("The country has been updated");
		
		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("Error: Could not connect to server or server encounter an error");
	});;
}

function addCountry() {
	if(!validateFormCountry()) return;
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	
	jsonData = {name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		showToastMessage("The new country has been added");
	}).fail(function() {
		showToastMessage("Error: Could not connect to server or server encounter an error");
	});
}

function selectNewlyAddedCountry(countryId, countryCode, countryName) {
	optionValue = countryId + "-" + countryCode;
	
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
	$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
	
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function changeFormStateToNew() {
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name: ")
	
	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);
	
	fieldCountryCode.val("").focus();
	fieldCountryName.val("").focus();
}

function changeFormStateToSelectedCountry() {
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);
	
	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);
	labelCountryName.text("Selected Country: ");
	
	countryCode = $("#dropDownCountries option:selected").val().split("-")[1];
	fieldCountryCode.val(countryCode);
}

function loadCountries() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry.empty();
		
		$.each(responseJSON, function(index, country) {
			optionValue = country.id + "-" + country.code;		
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
	}).done(function() {
		buttonLoad.val("Refesh contry list");
		showToastMessage("All countries has been loaded");
	}).fail(function() {
		showToastMessage("Error: Could not connect to server or server encounter an error");
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}

function validateFormCountry() {
	countriesForm = document.getElementById("countriesForm");
	if(!countriesForm.checkValidity()) {
		countriesForm.reportValidity();
		return false;
	}
	return true;
}
