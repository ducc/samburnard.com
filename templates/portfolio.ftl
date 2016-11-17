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
<div class="columns">
	<@item /><@item /><@item />
</div>
<div class="columns">
	<@item /><@item /><@item />
</div>
<div class="columns">
	<@item /><@item /><@item />
</div>
<div class="columns">
	<@item /><@item /><@item />
</div>
<div class="columns">
	<@item /><@item /><@item />
</div>
<div class="columns">
	<@item /><@item /><@item />
</div>
<div class="columns">
	<@item /><@item /><@item />
</div>
<div class="columns">
	<@item /><@item /><@item />
</div>
<div class="columns">
	<@item /><@item /><@item />
</div>
</@layout.layout>