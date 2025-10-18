htmx.on('htmx:afterRequest', function(evt) {
  if (evt.detail.elt.tagName === 'FORM') {
    evt.detail.elt.reset();
  }
});
