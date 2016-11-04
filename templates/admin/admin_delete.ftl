<#import "admin.ftl" as layout>
<#if project["title"]?has_content>
    <#assign title = project["title"] />
<#else>
    <#assign title = "this project" />
</#if>
<@layout.admin
tab_name="Are you sure you want to delete " + title + "?"
>
<a class="button is-danger is-large" href="/admin/delete/${project["id"]}/confirm">Delete Project</a>
<a class="button is-large" href="/admin/manage">Cancel</a>
</@layout.admin>