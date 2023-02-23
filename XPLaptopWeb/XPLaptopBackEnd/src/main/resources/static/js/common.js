$(document).ready(function() {
	customizeTabs();
});

function customizeTabs() {
	var url = document.location.toString();
	if(url.match('#')) {
		$("a[href='#" + url.split('#')[1] + "']").tab('show');
		console.log(url);
	}
	
	$('.nav-tabs a').on('show.bs.tab', function (e) {
		window.location.hash = e.target.hash;
	});
}