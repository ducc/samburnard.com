<#import "admin.ftl" as layout>
<#import "admin_add.ftl" as add>
<@layout.admin
tab_name="Edit a Project"
>
<form action="/admin/edit" method="post">
    <input name="id" style="display: none;" value="${project["id"]}">
    <label class="label">Project name</label>
    <p class="control">
        <input class="input" type="text" placeholder="The project's name" name="title" value="${project["title"]}">
    </p>
    <label class="label">Summary</label>
    <p class="control">
        <input class="input" type="text" placeholder="A small project summary" name="summary" value="${project["summary"]}">
    </p>
    <label class="label">Description</label>
    <p class="control">
        <textarea class="input" rows="3" placeholder="A 1 - 2 paragraph project description" name="description">${project["description"]}</textarea>
    </p>
    <label class="label">Main image</label>
    <p class="control">
        <input class="input" type="text" placeholder="A link to your main image" name="mainimage" value="${project["image"]}">
    </p>
    <label class="label">Project images</label>
    <span id="thumbnail-count" style="display: none;">${(project?size / 2) + 1}</span>
    <div class="control">
        <#if project?has_content>
            <#assign num = 0>
	        <#assign highest = project["images"]?size>
            <div class="columns">
			    <#list project["images"] as image>
			        <#assign num = num + 1>
			        <@add.item image[0] image[1] />
			        <#if num == 3>
			            </div>
	                    <div class="columns">
			            <#assign num = 0>
			        </#if>
		        </#list>
		        <#list 1..100 as while>
			        <#if highest % 3 == 0>
				        <#break />
			        </#if>
			        <#assign highest = highest + 1>
			        <@add.item highest "" />
		        </#list>
            </div>
            <div class="columns">
		        <#list 1..3 as x>
		            <#assign highest = highest + 1>
			        <@add.item highest "" />
		        </#list>
            </div>
            <div class="columns">
		        <#list 1..3 as x>
		            <#assign highest = highest + 1>
			        <@add.item highest "" />
		        </#list>
            </div>
        </#if>
    </div>
    <p class="control">
        <button class="button is-primary" type="submit">Update</button>
    </p>
</form>
</@layout.admin>