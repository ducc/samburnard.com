<#macro item
id="1371"
name="The Project Name"
image="http://placehold.it/640x480"
>
<div class="column is-one-third">
    <a href="/projects/${id}">
        <figure class="image is-4by3 hover-img" data-content="${name}">
            <img src="${image}" alt="${name}">
        </figure>
    </a>
</div>
</#macro>

<#import "layout.ftl" as layout>
<@layout.layout
title="Portfolio"
>
<#assign count=0>
<#list projects as project>
    <#if count=0>
        <div class="columns">
    </#if>
    <@item project["id"] project["title"] project["image"] />
    <#assign count=count+1 >
    <#if count=3>
        </div>
        <#assign count=0>
    </#if>
</#list>
</@layout.layout>