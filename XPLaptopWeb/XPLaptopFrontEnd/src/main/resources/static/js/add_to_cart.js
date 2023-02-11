$(document).ready(function() {
    $("#addToCart").on("click", function(event) {
        addToCart();
    })
});

function addToCart() {
    let quantity = $("#quantity"+productId).val();
    let url = contextPath + "cart/add/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        showModalDialog("Shopping cart", response);
    }).fail(function (response) {
        showErrorModal("Error while adding product to shopping cart.");
    });
}