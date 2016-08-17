<#macro layout
title="default"
>
<!DOCTYPE html>
<html>
    <head>
        <title>Sam Burnard | ${title}</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.1.2/css/bulma.min.css" type="text/css">
        <link rel="stylesheet" href="/styles.css" type="text/css">
    </head>
    <body>
        <section class="white">
            <div class="container">
                <#include "navbar.ftl" />
            </div>
        </section>
        <section class="container">
            <br />
            <#nested />
        </section>
        <script src="https://code.jquery.com/jquery-3.1.0.min.js"></script>
        <script src="/visual.js"></script>
    </body>
</html>
</#macro>