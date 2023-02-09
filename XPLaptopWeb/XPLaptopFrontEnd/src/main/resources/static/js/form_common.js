function checkPasswordMatch() {
    let password = $("#password").val();
    let confirmPassword = $("#confirmPassword").val();
    console.log(password);
    console.log(confirmPassword);
    console.log(password != confirmPassword);
    if(password != confirmPassword) {
        $("#confirmPassword").get(0).setCustomValidity("Password does not match");
        $("#confirmPassword").get(0).reportValidity();
    } else {
        $("#confirmPassword").get(0).setCustomValidity('');
    }
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal("show");
}