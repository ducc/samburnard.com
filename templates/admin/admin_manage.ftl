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
                        <a class="button is-warning" href="/admin/edit/${project["id"]}">
                            Edit
                        </a>
                        <a class="button is-danger" href="/admin/delete/${project["id"]}">
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