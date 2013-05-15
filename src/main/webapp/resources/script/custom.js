function hideTableDelete() {
    // TODO we will have some of the click listener dublicated, not a big deal but bad
    $('.ui-icon-pencil').click(function(e) {
        $(e.target).parents('td').children('.easy-remove').hide(250);
    });
    console.log('hideTableDelete attached ...')
}