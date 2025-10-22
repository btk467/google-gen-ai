@import gg.jte.html.HtmlContent

@param HtmlContent content

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Google Gen AI</title>
    <link rel="stylesheet" href="css/pico.min.css">
    <link rel="stylesheet" href="css/style.css">
    <script src="js/htmx.min.js"></script>
    <script src="js/app.js"></script>
</head>
<body>
<main class="container">
    <div id="chat-container">
        <div class="chat-response">Welcome! I am AI assistant. How can I help you today?</div>
    </div>
    <form id="chat-form" hx-post="/chat" hx-target="#chat-container" hx-swap="beforeend">
        <input type="text" id="chat-input" name="message" placeholder="Enter your message" required>
       <div>
            <button id="send-button" type="submit">Send</button>
        </div>    
		</form>
</main>
</body>
</html>