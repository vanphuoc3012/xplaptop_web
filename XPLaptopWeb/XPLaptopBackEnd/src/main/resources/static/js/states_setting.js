var buttonLoadCountries4States;
var dropDownCountry4States;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function() {
	buttonLoadCountries4States = $("#buttonLoadCountriesForStates");
	dropDownCountry4States = $("#dropDownCountriesForState");
	dropDownStates = $("#dropDownStates");
	buttonAddState = $("#btnAddState");
	buttonUpdateState = $("#btnUpdateState");
	buttonDeleteState = $("#btnDeleteState");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");
	
	buttonLoadCountries4States.click(function() {
		loadCountries4States();
	});
	
	dropDownCountry4States.on("change", function() {
		loadStatesByCountry();
	});
	
	dropDownStates.on("change", function() {
		changeFormStateToSelectedState();
	});
	
	buttonAddState.on("click", function() {
		if(buttonAddState.val() == "New") {
			changeFormStateToAddNewState();
		} else {
			addNewState();
		}
	});
	
	buttonUpdateState.on("click", function() {
		updateSelectedState();
	});
	
	buttonDeleteState.on("click", function() {
		deleteSelectedState();
	});
});

function deleteSelectedState() {
	selectedState = $("#dropDownStates option:selected");
	stateId = selectedState.val();
	
	url = contextPath + "states/delete/" + stateId; 
	
	$.get(url, function() {
		selectedState.remove();
	}).done(function() {
		showToastMessage("The state has been deleted");
		
		changeFormStateToAddNewState();
	}).fail(function() {
		showToastMessage("Error: Could not connect to server or server encounter an error");
	});
}

function updateSelectedState() {
	if(!validateFormStates()) return;
	url = contextPath + "states/save";
	selectedState = $("#dropDownStates option:selected");
	
	stateName = fieldStateName.val();
	stateId = selectedState.val();
	
	selectedCountry = $("#dropDownCountriesForState option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	
	jsonData = {
		id: stateId,
		name: stateName,
		country: {
			id: countryId
		}
	};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue)},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectedState.text(stateName);
		
		showToastMessage("The state has been updated");
		
		changeFormStateToAddNewState();
	}).fail(function() {
		showToastMessage("Error: Could not connect to server or server encounter an error");
	});
}

function addNewState() {
	if(!validateFormStates()) return;
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	
	selectedCountry = $("#dropDownCountriesForState option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	if(countryId == null) {
		document.getElementById("dropDownCountriesForState").focus();
		return;
	}
	jsonData = {
		name: stateName,
		country: {
			id: countryId
		}
	};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue)},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectNewlyAddState(stateId, stateName);
		showToastMessage("The new state has been added");
	}).fail(function() {
		showToastMessage("Error: Could not connect to server or server encounter an error");
	});
}

function selectNewlyAddState(stateId, stateName) {
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
	$("#dropDownStates option[value=' " + stateId + "']").prop("selected", true);
	
	fieldStateName.val("").focus();
}

function changeFormStateToAddNewState() {
	buttonAddState.val("Add");
	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);
	
	labelStateName.text("State name: ");
	fieldStateName.val("").focus();
}

function changeFormStateToSelectedState() {
	buttonAddState.val("New");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);
	
	labelStateName.text("Selected State/Province: ");
	
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName).focus();
}

function loadStatesByCountry() {
	selectedCountry = $("#dropDownCountriesForState option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	
	url = contextPath + "states/list_by_country/" + countryId;
	$.get(url, function(responseJSON) {
		dropDownStates.empty();
		
		$.each(responseJSON, function(index, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});
	}).done(function() {
		showToastMessage("All states of "+ countryName +" has been loaded");
	}).fail(function() {
		showToastMessage("Error: Could not connect to server or server encounter an error");
	});
}

function loadCountries4States() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry.empty();
		
		$.each(responseJSON, function(index, country) {
			optionValue = country.id;	
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry4States);
		});
	}).done(function() {
		buttonLoadCountries4States.val("Refresh country list");
		showToastMessage("All countries has been loaded");
	}).fail(function() {
		showToastMessage("Error: Could not connect to server or server encounter an error");
	});
}

function validateFormStates() {
	statesForm = document.getElementById("statesForm");
	if(!statesForm.checkValidity()) {
		statesForm.reportValidity();
		return false;
	}
	return true;
}