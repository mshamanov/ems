function confirmDelete(obj) {
    const result = confirm("Are you sure you want to delete " + obj.name + "?");
    if (result) {
        let pathName = window.location.pathname;
        pathName = pathName.replace(/^(\/[a-z])(\w+?)(s$)/i, (_, first, middle, end) => first.slice(1).toUpperCase() + middle.toLowerCase());
        if (pathName == '/') {
            pathName = 'Employee';
        }
        window.location.href = '/delete' + pathName + '?id=' + obj.id;
    }
}

function sortBy(newSortBy, currentSortBy, currentSortDir) {
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('direction', currentSortBy === newSortBy ? (currentSortDir === 'asc' ? 'desc' : 'asc') : 'asc');
    urlParams.set('sortBy', newSortBy)
    window.location.href = `${location.pathname}?${urlParams}`;
}

function goToPage(pageNumber, pageSize) {
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('page', pageNumber);
    urlParams.set('size', pageSize);
    window.location.href = `${location.pathname}?${urlParams}`;
}