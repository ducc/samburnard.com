<#import "admin.ftl" as layout>
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
    <span id="thumbnail-count" style="display: none;">${project?size / 2}</span>
    <div id="thumbnail-inputs">
        <#list project["images"] as image>
            <div id="item" class="box">
                <p class="control">
                    <input class="input add-image-input" type="text" placeholder="Thumbnail link" name="image_thumbnail_${image[0]}" value="${image[1]}">
                </p>
                <p class="control">
                    <input class="input add-image-input" type="text" placeholder="Image link" name="image_image_${image[0]}" value="${image[2]}">
                </p>
            </div>
        </#list>
    </div>
    <br />
    <a class="button is-success is-fullwidth" id="add-image">
        Add another image
    </a>
    <br />
    <p class="control">
        <button class="button is-primary" type="submit">Update</button>
    </p>
</form>
</@layout.admin>