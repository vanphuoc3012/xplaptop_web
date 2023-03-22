let fieldProductCost;
let fieldSubtotal;
let fieldShippingCost;
let fieldTax;
let fieldTotal;

$(document).ready(function () {
    fieldProductCost = $("#productCost");
    fieldSubtotal = $("#subtotal");
    fieldShippingCost = $("#shippingCost");
    fieldTax = $("#tax");
    fieldTotal = $("#total");

    formatOderAmounts();
    formatProductAmounts();

    $("#productList").on('change', '.quantity-input', function (e) {
        updateSubtotalWhenQuantityChange($(this));
        updateOrderAmounts();
    });

    $("#productList").on('keyup', '.price-input', function (e) {
        updateSubtotalWhenPriceUnitChange($(this));
        updateOrderAmounts();
    });

    $("#productList").on('keyup', '.ship-input', function (e) {
        updateOrderAmounts();
    });

    $("#productList").on('keyup', '.cost-input', function (e) {
        updateOrderAmounts();
    });
});

function updateOrderAmounts() {
    let totalCost = 0.0;
    console.log("Re-calculate product cost");
    $(".cost-input").each(function () {
        let rowNumber = $(this).attr('rowNumber');
        let quantityValue = $("#quantity" + rowNumber).val();
        let costPerProduct = getNumberValueAndRemovedThousandSeparator($(this));

        totalCost += costPerProduct * quantityValue;
    });
    setAndFormatNumberForField("productCost", totalCost);

    let subTotal = 0.0;
    console.log("Re-calculate subtotal");
    $(".subtotal-output").each(function (e) {
        let productSubtotalValue = getNumberValueAndRemovedThousandSeparator($(this));
        subTotal += productSubtotalValue;
    });
    setAndFormatNumberForField("subtotal", subTotal);

    let shippingCost = 0.0;
    console.log("Re-calculate shipping cost");
    $(".ship-input").each(function (e) {
        let shipPerProductValue = getNumberValueAndRemovedThousandSeparator($(this));
        shippingCost += shipPerProductValue;
    });
    setAndFormatNumberForField("shippingCost", shippingCost);

    let tax = getNumberValueAndRemovedThousandSeparator(fieldTax)
    let orderTotal = subTotal + tax + shippingCost;
    setAndFormatNumberForField("total", orderTotal);
}

function setAndFormatNumberForField(fieldId, fieldValue) {
    let formattedValue = $.number(fieldValue, currencyDigits, decimalPointType, thousandPointType);
    $("#"+fieldId).val(formattedValue);
}

function getNumberValueAndRemovedThousandSeparator(fieldRef) {
    let fieldValue = fieldRef.val();
    if(thousandPointType === ",") {
        fieldValue = fieldValue.replace(",", "");
    }
    if(decimalPointType === ",") {
        fieldValue = fieldValue.replace(".", "");
        fieldValue = fieldValue.replace(",", ".");
    }
    return parseFloat(fieldValue);
}

function updateSubtotalWhenPriceUnitChange(input) {
    let unitPriceValue = getNumberValueAndRemovedThousandSeparator(input);
    console.log('Unit price: ' + unitPriceValue);
    let rowNumber = input.attr('rowNumber');
    let quantityValue = $("#quantity"+rowNumber).val();
    let newSubtotalValue = quantityValue * unitPriceValue;

    setAndFormatNumberForField("subtotal"+rowNumber, newSubtotalValue);
}
function updateSubtotalWhenQuantityChange(input) {
    let quantityValue = input.val();
    console.log('Quantity: ' + quantityValue);
    let rowNumber = input.attr('rowNumber');
    let priceField = $("#price"+rowNumber);
    let unitPriceValue = getNumberValueAndRemovedThousandSeparator(priceField);
    let newSubtotalValue = quantityValue * unitPriceValue;

    setAndFormatNumberForField("subtotal"+rowNumber, newSubtotalValue)
}
function formatProductAmounts() {
    $(".cost-input").each(function (e) {
        formatNumberForField($(this));
    });
    $(".price-input").each(function (e) {
        formatNumberForField($(this));
    });
    $(".subtotal-output").each(function (e) {
        formatNumberForField($(this));
    });
    $(".ship-input").each(function (e) {
        formatNumberForField($(this));
    });
}

function formatOderAmounts() {
    formatNumberForField(fieldProductCost);
    formatNumberForField(fieldSubtotal);
    formatNumberForField(fieldShippingCost);
    formatNumberForField(fieldTax);
    formatNumberForField(fieldTotal);
};

function formatNumberForField(fieldRef) {
    fieldRef.val($.number(fieldRef.val(), currencyDigits, decimalPointType, thousandPointType));
}

