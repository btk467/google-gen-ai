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
});

htmx.on('htmx:afterSettle', function(evt) {
    const chatInput = document.getElementById('chat-input');
    if (chatInput) {
        chatInput.focus();
    }
});

function copyToClipboard(button) {
  const article = button.parentElement;
  const textToCopy = article.querySelector('div').innerText;
  navigator.clipboard.writeText(textToCopy).then(() => {
    button.innerText = 'Copied!';
    setTimeout(() => {
      button.innerText = 'Copy';
    }, 2000);
  });
}

