@param String question
<article class="chat-response user-question">${question}</article>

<article class="chat-response" 
  hx-get="/chat/response?message=${question}" 
  hx-trigger="load" 
  hx-swap="outerHTML">
  <span aria-busy="true">Working on it ...</span>
</article>
  