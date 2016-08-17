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
        <tr>
            <td>1733</td>
            <td>My Project</td>
            <td>
                <a class="button" href="/projects/1733">
                    View
                </a>
                <a class="button is-warning" href="/admin/manage/1733">
                    Edit
                </a>
                <a class="button is-danger" href="/admin/manage/1733/delete">
                    Delete
                </a>
            </td>
        </tr>
    </tbody>
</table>
</@layout.admin>