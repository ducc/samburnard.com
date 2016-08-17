<#import "layout.ftl" as layout>
<@layout.layout
title="Error"
>
<br />
<br />
<br />
<center>
    <h1 class="title">Error: ${code}</h1>
    <h2 class="subtitle">${message}</h2>
</center>
</@layout.layout>