var buttonLoad;
var dropDownCountry;

$(document).ready(function() {
	buttonLoad = $("#buttonLoadCountries");  // input: Load Country List
	dropDownCountry = $("#dropDownCountries"); // hàm select
	
	buttonLoad.click(function() {
		loadCountries();
	});
});

function loadCountries() {
	// alert("About loading countries..");
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry.empty();
		
		$.each(responseJSON, function(index, country) {
			optionValue = country.id + "-" + country.code;
			//alert(optionValue);
		    $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
	}).done(function() {
		buttonLoad.val("Refresh Country List");
		showToastMessage("All countries have been loaded");
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$('.toast').toast('show');
}
