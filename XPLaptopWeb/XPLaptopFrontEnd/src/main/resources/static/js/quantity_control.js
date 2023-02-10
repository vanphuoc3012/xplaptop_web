$(document).ready(function() {
    $(".linkMinus").on("click", function(event) {
        event.preventDefault();
        let productId = $(this).attr("pid");
        let quantityInput = $("#quantity" + productId);
        let newQuantity = parseInt(quantityInput.val()) - 1;
        if(newQuantity > 0) {
            quantityInput.val(newQuantity);
        } else {
            quantityInput.val(1);
        }
    });

    $(".linkPlus").on("click", function(event) {
        event.preventDefault();
        let productId = $(this).attr("pid");
        let quantityInput = $("#quantity" + productId);
        let newQuantity = parseInt(quantityInput.val()) + 1;
        if(newQuantity <= 5) {
            quantityInput.val(newQuantity);
        } else {
            quantityInput.val(5);
        }
    });
})