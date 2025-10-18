htmx.on('htmx:afterRequest', function(evt) {
  if (evt.detail.elt.tagName === 'FORM') {
    evt.detail.elt.reset();
  }
});

htmx.on('htmx:afterSwap', function(evt) {
    const container = document.querySelector('.container');
    if (container) {
        container.scrollTop = container.scrollHeight;
    }

    const chatInput = document.getElementById('chat-input');
    if (chatInput) {
        chatInput.focus();
    }
});