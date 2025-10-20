@import gg.jte.html.HtmlContent
@param HtmlContent content

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>HTMX SSE Demo</title>
    <script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.7/dist/htmx.min.js" integrity="sha384-ZBXiYtYQ6hJ2Y0ZNoYuI+Nq5MqWBr+chMrS/RkXpNzQCApHEhOt2aY8EJgqwHLkJ" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/htmx-ext-sse@2.2.4" integrity="sha384-A986SAtodyH8eg8x8irJnYUk7i9inVQqYigD6qZ9evobksGNIXfeFvDwLSHcp31N" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/webjars/pico/css/pico.min.css"/>
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }
        
        .time {
            font-size: 4rem;
            font-weight: bold;
            font-family: monospace;
        }
    </style>
</head>
<body>
  <main class="container">
    <h1>Current Time</h1>
    <div class="time-slot" 
      hx-ext="sse" 
      sse-connect="/time/stream" 
      sse-swap="message">
      --:--:--
    </div>  
  </main>
</body>
</html>
