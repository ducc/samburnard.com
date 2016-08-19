<#import "admin.ftl" as layout>
<@layout.admin
tab_name="Manage Projects"
>
<table class="table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <#if projects?has_content>
            <#list projects as project>
                <tr>
                    <td>${project["id"]}</td>
                    <td>${project["title"]}</td>
                    <td>
                        <a class="button" href="/projects/${project["id"]}">
                            View
                        </a>
                        <a class="button is-warning" href="/admin/manage/${project["id"]}">
                            Edit
                        </a>
                        <a class="button is-danger" href="/admin/manage/${project["id"]}/delete">
                            Delete
                        </a>
                    </td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td>No projects found!</td>
            </tr>
        </#if>
    </tbody>
</table>
</@layout.admin>