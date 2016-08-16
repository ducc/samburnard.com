<#macro item
id="1371"
name="The Project Name"
image="http://placehold.it/640x480"
>
<div class="column is-one-third">
    <a href="/projects/${id}">
        <figure class="image is-4by3">
            <img src="${image}" alt="${name}">
        </figure>
    </a>
</div>
</#macro>

<#assign items=[
{
"id": "1371",
"name": "The Project Name",
"image": "http://placehold.it/640x480"
},
{
"id": "1371",
"name": "The Project Name",
"image": "http://placehold.it/640x480"
},
{
"id": "1371",
"name": "The Project Name",
"image": "http://placehold.it/640x480"
},
{
"id": "1371",
"name": "The Project Name",
"image": "http://placehold.it/640x480"
},
{
"id": "1371",
"name": "The Project Name",
"image": "http://placehold.it/640x480"
},
{
"id": "1371",
"name": "The Project Name",
"image": "http://placehold.it/640x480"
},
{
"id": "1371",
"name": "The Project Name",
"image": "http://placehold.it/640x480"
},
{
"id": "1371",
"name": "The Project Name",
"image": "http://placehold.it/640x480"
}
]>

<#import "layout.ftl" as layout>
<@layout.layout
title="Home"
>
<#assign count=0>
<#list items as obj>
    <#if count=0>
        <div class="columns">
    </#if>
    <@item obj.id obj.name obj.image />
    <#assign count=count+1 >
    <#if count=3>
        </div>
        <#assign count=0>
    </#if>
</#list>
</@layout.layout>