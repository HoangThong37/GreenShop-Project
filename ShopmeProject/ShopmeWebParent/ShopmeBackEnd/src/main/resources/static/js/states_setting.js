var buttonLoadStates;
 var dropDownCountriesForStates;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;

var labelStateName;
var fieldStateName;


$(document).ready(function() {
	buttonLoadStates = $("#buttonLoadCountriesForStates");  // input: Load states List
	
	dropDownCountriesForStates = $("#dropDownCountriesForStates"); // hàm select
	dropDownStates  = $("#dropDownStates");
	buttonAddState = $("#buttonAddState");  
	buttonUpdateState = $("#buttonUpdateState");   // id of input
	buttonDeleteState = $("#buttonDeleteState"); 
	
	labelStateName  = $("#labelStateName");
	fieldStateName  = $("#fieldStateName"); 
	
	buttonLoadStates.click(function() {
		loadCountriesForStates();
	});
	
	dropDownCountriesForStates.on("change", function() { // .on(): Đính kèm một hàm xử lý sự kiện cho một hoặc nhiều sự kiện tới một thành phần được chọn.
		loadStateForCountry();
	});
	
	dropDownStates.on("change", function() { // .on(): Đính kèm một hàm xử lý sự kiện cho một hoặc nhiều sự kiện tới một thành phần được chọn.
		changeFormStateToSelectedCountries();
	});
	
	buttonAddState.click(function() {  // click vào nút Add
	  if(buttonAddState.val() == "Add") {  // add: value="add"
		  addState();
	  } else {
		  changeFormStateToNew();
	  }  
	});
	
	buttonUpdateState.click(function() {
		updateState();
	});
	
	buttonDeleteState.click(function() {
		deleteState();
	});
});

// call delete countries
function deleteState() {
	stateId = dropDownStates.val();
	
	url = contextPath + "states/delete/" + stateId;
	
	
	$.ajax({
		type: 'DELETE', 
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfHeaderValue);
		}
	}).done(function() {
		$("#dropDownStates option[value= '" + stateId + "']").remove();
		changeFormStateToNew();
		showToastMessage("The states has been deleted");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

// call update State
function updateState() {
	if(!validateState()) return;  //  validate : xác thực
	
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	stateId = dropDownStates.val(); // 

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();   
    countryName = selectedCountry.text(); 
	
	jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};
	
	$.ajax({
		type: 'POST', 
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfHeaderValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		// $("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownStates option:selected").text(stateName);  // hiển thị trong form đã thay đổi
		showToastMessage("The state has been updated");
		
		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}


function validateState() {
	// getElementById() sẽ trả về phần tử có thuộc tính id được chỉ định
	formState = document.getElementById("formState");
	if(!formState.checkValidity()) {
		formState.reportValidity(); // show error message
		return false;
	}
	return true;
	// checkValidity() -> Phương thức này return false, nếu có một hoặc nhiều validation error trong form ngược lại nó return true.
}


// call add new countries
function addState() {
	if(!validateState()) return;  //  validate : xác thực

	url = contextPath + "states/save";
	//countryName = fieldCountryName.val();
	stateName = fieldStateName.val();
	
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();   
    countryName = selectedCountry.text();       
    
	jsonData = {name: stateName, country: {id: countryId, name: countryName}};
	
	$.ajax({
		type: 'POST', 
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfHeaderValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		//alert("new add country ID : " + countryId);

		selectNewAddState(stateId, stateName);
		showToastMessage("The new state has been added");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function selectNewAddState(stateId, stateName) {
	    // hiển thị all + new state in form
		$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);	
		 
		$("#dropDownStates option[value= '" + stateId + "']").prop("selected", true); // prop: lấy thuộc tính của thẻ
		
		fieldStateName.val("").focus();
}
	

function changeFormStateToSelectedCountries() {
	buttonAddState.prop("value", "New");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);
	 
	labelStateName.text("Selectted State:")

	// nhập vào thì nó in vào ra chỗ input: country name
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);

}

 //1. load countries states
function loadCountriesForStates() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry.empty();
		
		$.each(responseJSON, function(index, country) {
			//optionValue = country.id + "-" + country.code;
			//alert(optionValue);
		    $("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStates);
		});
	}).done(function() {
		buttonLoadStates.val("Refresh Country List");
		showToastMessage("All countries have been loaded");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

 //2. load dropDownCountriesForStates
function loadStateForCountry() {
	// alert("About loading countries..");
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	url = contextPath + "states/list_by_country/" + countryId;
	
	$.get(url, function(responseJSON) {
		dropDownStates.empty();  // 
		
		$.each(responseJSON, function(index, state) {
			// optionValue = country.id + "-" + country.code;
			// alert(optionValue);
		    $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});
	}).done(function() {
		changeFormStateToNew();
		showToastMessage("All state have been loaded country " + selectedCountry.text());
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function changeFormStateToNew() {
	buttonAddState.val("Add");
	labelStateName.text("State/Province Name:")
	
	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);
	
	fieldStateName.val("").focus();
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$('.toast').toast('show');
}
