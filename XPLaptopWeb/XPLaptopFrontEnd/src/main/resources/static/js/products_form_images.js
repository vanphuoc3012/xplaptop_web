$(document).ready(function() {
	// preview photo			
	$(".uploadImage").change(function() {
		fileSize = this.files[0].size;
		sizeInMb = fileSize/1048576
		if(fileSize > 1048576) {
		//alert("Size of image must less than 1Mb. Your image: "+sizeInMb.toFixed(2)+" Mb");
			this.setCustomValidity("Size of image must less than 1Mb. Your image: "+sizeInMb.toFixed(2)+" Mb");
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			showImgThumbnail(this);
			if($(this).attr('class').includes('extraImage') && !$(this).parent().next().length) {
				addExtraImage(this);
			}
		}
	});
});	
	
function resetThumnail(btn) {
	if($(btn).parent().next() == null) {
		src = defaultImg;
		clone = $(btn).prev().clone().attr("src", src);
		$(btn).prev().remove();
		clone.insertBefore($(btn));
		$(btn).next().next().val('');
	} else {
		$(btn).parent().remove();
	}
}

function checkImgSize(curretnInput) {
	fileSize = curretnInput.files[0].size;
	sizeInMb = fileSize/1048576
	if(fileSize > 1048576) {
	//alert("Size of image must less than 1Mb. Your image: "+sizeInMb.toFixed(2)+" Mb");
		curretnInput.setCustomValidity("Size of image must less than 1Mb. Your image: "+sizeInMb.toFixed(2)+" Mb");
		curretnInput.reportValidity();
	} else {
		curretnInput.setCustomValidity("");
		showImgThumbnail(curretnInput);
		if($(curretnInput).attr('class').includes('extraImage') && !$(curretnInput).parent().next().length) {
			addExtraImage(curretnInput);
		}
	}
}

/* add extra image, current input = type file */
function addExtraImage(currentInput) {
	numberOfExtraImages = $(currentInput).parent().parent().length;
	
	if(numberOfExtraImages < 10) {
		html = `
		<div class="row col-sm-3 justify-content-center ms-1 mt-2">									
			<img class="img-thumbnail" alt="Preview photo" src="${defaultImg}" style="width: 200px"/>
			<button type="button" class="btn-close delImage" onclick="resetThumnail(this)"></button>
			<p class="fw-bold text-center"><small>Extra image (optional)</small> </p>			
			<input class="form-control form-control-sm uploadImage extraImage" type="file" name="newExtraImage" 
			accept="image/png, image/jpeg" onchange="extraImgFunc(this)"/>
		</div>
	`;
		$(html).insertAfter($(currentInput).parent());
	} else {
		html = `
			<p class="fw-bold">Maxium 10 extra images</p>
		`;
		$(html).insertAfter($(parentOfCurrentInput));
	}	
}

function extraImgFunc(currentInput) {
	checkImgSize(currentInput);
}