$(document).ready(function() {
    $(".linkMinus").on("click", function(event) {
        event.preventDefault();
        decreaseQuantity($(this));
    });

    $(".linkPlus").on("click", function(event) {
        event.preventDefault();
        increaseQuantity($(this));
    });

    $(".link-remove").on("click", function(event) {
        event.preventDefault();
        deleteProduct($(this));
    });
});

function deleteProduct(link) {
    let url = link.attr("href");

    $.ajax({
        type: "DELETE",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        let rowNumber = link.attr("rowNumber");
        deleteProductHTML(rowNumber);
        updateTotal();
        updateCountNumber();
    }).fail(function (response) {
        showErrorModal("Error while deleting product to shopping cart.");
    });
}

function updateCountNumber() {
    $(".divCount").each(function (index, element) {
        element.innerHTML = "" + (index + 1);
    });
}

function deleteProductHTML(rowNumber) {
    $("#row" + rowNumber).remove();
}

function decreaseQuantity(link) {
    let productId = link.attr("pid");
    let quantityInput = $("#quantity" + productId);
    let newQuantity = parseInt(quantityInput.val()) - 1;
    if(newQuantity > 0) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity);
    } else {
        quantityInput.val(1);
    }
}

function increaseQuantity(link) {
    let productId = link.attr("pid");
    let quantityInput = $("#quantity" + productId);
    let newQuantity = parseInt(quantityInput.val()) + 1;
    if(newQuantity <= 5) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity);
    } else {
        quantityInput.val(5);
    }
}

function updateQuantity(productId, quantity) {
    let url = contextPath + "cart/update/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (updatedSubtotal) {
        updateSubtotal(updatedSubtotal, productId);
        updateTotal();
    }).fail(function (response) {
        showErrorModal("Error while adding product to shopping cart.");
    });
}

function updateSubtotal(updatedSubtotal, productId) {
    let formattedSubtotal = $.number(Number(updatedSubtotal), currencyDigits, decimalPointType, thousandPointType);
    $("#subtotal"+productId).attr("subTotalValue", updatedSubtotal);
    $("#subtotal"+productId).text(formattedSubtotal);
}

function updateTotal() {
    let total = 0.0;
    let productCount = 0;
    $(".subtotal").each(function (index, element) {
        productCount++;
        total += parseFloat($(this).attr("subTotalValue"));
    });
    if(productCount < 1) {
        showEmptyShoppingCart();
    }
    let formattedTotal = $.number(total, currencyDigits, decimalPointType, thousandPointType);
    $("#estimatedTotal").text(formattedTotal);
}

function showEmptyShoppingCart() {
    $("#sectionTotal").hide();
    $("#emptyCartMessage").removeClass("d-none");
}