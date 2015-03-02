function hideTableDelete() {
    // TODO we will have some of the click listener dublicated, not a big deal but bad
    $('.ui-icon-pencil').click(function(e) {
        $(e.target).parents('td').children('.easy-remove').hide(250);
    });
    console.log('hideTableDelete attached ...');
}

function hideDialog(args, dialog) {
    if (!args) throw 'Missing stats args, cannot determine what to do...';
    if (!dialog) throw 'Dialog not given, cannot hide it...';
    if (!args.validationFailed) dialog.hide(); else dialog.show();
}

/*
window.onpopstate = function(e) {
    if (e.state && window.location.href !== e.state.url) {
        //alert(e.state.url);
        window.location.href = e.state.url;
    }
    //perhaps use an ajax call to update content based on the e.state.id
};

function addToHistory(url) {
    if (window.history && window.history.pushState) {
        console.log("we are here: " + location.href);
        if (location.href !== url) window.history.pushState({'url' : url}, document.title, url);
    }
}
*/
// if (!args.validationFailed) createTeamDialog.hide(); else createTeamDialog.show();