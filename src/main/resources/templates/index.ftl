<#import "layout.ftl" as layout>
<@layout.basic
title="Illustration and Design"
index_script=true
>
<span style="display: none;" id="carousel-data">${json}</span>
<style>
    html,
    body {
    height: 100%;
    }

    body {
        background-image: url("${json?eval.images[0]}");
        background-repeat: no-repeat;
        background-attachment: fixed;
        background-position: center;
        -webkit-background-size:;
        background-size: contain;
    }

    .index-hero {
        margin-top: 300px;
        font-size: 50px;
        color: white;
        font-weight: 30px;
        letter-spacing: 5px;
    }
</style>
<section class="white">
    <div class="container">
        <#include "navbar.ftl" />
    </div>
</section>
</@layout.basic>