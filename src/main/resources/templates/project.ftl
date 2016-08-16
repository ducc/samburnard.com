<#macro item
id="-1"
name="Default name"
thumbnail="Default thumbnail"
image="Default image"
>
<div class="column is-one-third">
    <a class="modal-item" data-toggle="#${id}-modal">
        <figure class="image is-4by3">
            <img src="${thumbnail}" alt="${name}">
        </figure>
    </a>
    <div class="modal" id="${id}-modal">
        <div class="modal-background"></div>
        <div class="modal-content">
            <p class="image is-4by3">
                <img src="${image}">
            </p>
        </div>
        <button class="modal-close"></button>
    </div>
</div>
</#macro>

<#assign items=[
{
"id": "678924",
"name": "An inspirational image",
"thumbnail": "http://placehold.it/640x480",
"image": "http://placehold.it/1920x1080"
}
]>

<#assign project_title="Demo Project" />
<#assign project_summary="This is a short and awesome project description" />
<#assign project_description="This is a really long and super duper interesting project description that I took a very long time to write. It has punctuation and also features aspects of grammar. Please hire me!" />

<#import "layout.ftl" as layout>
<@layout.layout
title=project_title
>
<div class="columns">
    <div class="column is-one-quarter">
        <div class="box">
            <h1 class="title">${project_title}</h1>
            <h2 class="subtitle">${project_summary}</h2>
            <p>
                ${project_description}
            </p>
        </div>
    </div>
    <div class="column">
        <#assign count=0>
        <#list items as obj>
            <#if count=0>
                <div class="columns">
            </#if>
            <@item obj.id obj.name obj.thumbnail obj.image />
            <#assign count=count+1 >
            <#if count=3>
            </div>
                <#assign count=0>
            </#if>
        </#list>
    </div>
</div>
</@layout.layout>