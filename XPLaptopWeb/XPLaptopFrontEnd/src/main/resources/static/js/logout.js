$(document).ready(function() {
    $("#logoutLink").on("click", function(e) {
        e.preventDefault();
        $("#logoutForm").submit();
    });
});