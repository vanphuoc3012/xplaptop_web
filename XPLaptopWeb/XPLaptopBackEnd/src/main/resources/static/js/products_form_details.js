function addDetailSection(btnAddDetail) {
	html = `
			<div class="row border m-1 rounded">
				<div class="row col-sm m-1">
					<label class="form-label mb-1 col-sm-2 align-items-center">Name:</label>
					<input class="form-control col-sm" type="text" name="detailNames" maxlength="256">
				</div>
				
				<div class="row col-sm m-1">
					<label class="form-label mb-1 col-sm-2 align-items-center">Value:</label>
					<input class="form-control col-sm" type="text" name="detailValues" maxlength="512">
				</div>
				
				<div class="col-sm-1 row align-items-center"></div>	
			</div>
				
	`;
	
	btnDeleteDetailSection = `
			<a href="#" onclick="deleteThisSection(this)" class="fa-solid fa-circle-xmark fa-2x"></a>
	`;
	$(btnDeleteDetailSection).appendTo($(btnAddDetail).parent().prev().children().last());
	$(html).insertAfter($(btnAddDetail).parent().prev());
}

function deleteThisSection(btnDeleteThisSection) {
	$(btnDeleteThisSection).parent().parent().remove();
}
