<#import "admin.ftl" as layout>
<@layout.admin
tab_name="Are you sure you want to delete " + project["title"] + "?"
>
    <a class="button is-danger is-large" href="/admin/delete/${project["id"]}/confirm">Delete Project</a>
    <a class="button is-large" href="/admin/manage">Cancel</a>
</@layout.admin>