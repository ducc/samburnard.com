<#macro basic
title="default"
index_script=false
>
<!DOCTYPE html>
<html>
<head>
    <title>Sam Burnard | ${title}</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" type="text/css">
    <link rel="stylesheet" href="/bulma.css" type="text/css">
    <link rel="stylesheet" href="/styles.css" type="text/css">
</head>
<body>
    <#nested />
    <script src="https://code.jquery.com/jquery-3.1.0.min.js"></script>
    <script src="/visual.js"></script>
    <#if index_script>
        <script src="/index.js"></script>
    </#if>
    <script src="//lightwidget.com/widgets/lightwidget.js"></script>
</body>
</html>
</#macro>

<#macro layout
title="default"
>
<@basic
title=title
>
<section class="white">
    <div class="container">
        <#include "navbar.ftl" />
    </div>
</section>
<section class="container">
    <br />
    <#nested />
</section>
<footer class="footer">
    <div class="container">
        <div class="content has-text-centered">
            <p>
                Copyright &copy; ${.now?string("yyyy")} Sam Burnard
            </p>
            <p>
                <a class="icon" href="https://github.com/sponges/samburnard.com">
                    <i class="fa fa-github"></i>
                </a>
            </p>
        </div>
    </div>
</footer>
</@basic>
</#macro>