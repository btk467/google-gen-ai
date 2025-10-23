@param String question
<article class="chat-response error-response">
  <div>Sorry, something went wrong. Please try again.</div>
  <button hx-get="/chat/response?message=${question}" hx-target="closest article" hx-swap="outerHTML">Retry</button>
</article>
