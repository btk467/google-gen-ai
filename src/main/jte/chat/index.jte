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
    <script src="js/htmx-ext-sse.js"></script>
    <script src="js/app.js"></script>
</head>
<body>
<main class="container">
    <div id="chat-container">
    </div>
    <form hx-post="/chat" hx-target="#chat-container" hx-swap="beforeend">
        <input type="text" name="message" placeholder="Enter your message">
        <div>
            <button type="submit">Send</button>
        </div>
    </form>
</main>
</body>
</html>
