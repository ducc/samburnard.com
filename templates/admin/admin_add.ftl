<#macro item id value>
	<div class="column is-4">
	    <p class="control">
	        <input class="input add-image-input" type="text" placeholder="Image link" name="image_${id}" <#if value?has_content>value="${value}"</#if>>
	    </p>
	</div>
</#macro>

<#macro items ids>
	<div class="columns">
		<#list ids as id>
			<@item id "" />
		</#list>
    </div>
</#macro>

<#import "admin.ftl" as layout>
<@layout.admin
tab_name="Add a Project"
>
<form action="/admin/add" method="post">
    <label class="label">Project name</label>
    <p class="control">
        <input class="input" type="text" placeholder="The project's name" name="title">
    </p>
    <label class="label">Summary</label>
    <p class="control">
        <input class="input" type="text" placeholder="A small project summary" name="summary">
    </p>
    <label class="label">Description</label>
    <p class="control">
        <textarea class="input" placeholder="A 1 - 2 paragraph project description" name="description"></textarea>
    </p>
    <label class="label">Main image</label>
    <p class="control">
        <input class="input" type="text" placeholder="A link to your main image" name="mainimage">
    </p>
    <label class="label">Project images</label>
	<div class="control">
		<@items [1,  2,  3] />
        <@items [4,  5,  6] />
        <@items [7,  8,  9] />
        <@items [10, 11, 12] />
        <@items [13, 14, 15] />
    </div>
    <p class="control">
        <button class="button is-primary" type="submit">Create project</button>
    </p>
</form>
</@layout.admin>