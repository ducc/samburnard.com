<#import "admin.ftl" as layout>
<@layout.admin
tab_name="Add a Project"
>
<form action="#" method="post">
    <label class="label">Project name</label>
    <p class="control">
        <input class="input" type="text" placeholder="The project's name" name="name">
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
    <div id="thumbnail-inputs">
        <div id="item" class="box">
            <p class="control">
                <input class="input" type="text" placeholder="Thumbnail link" name="image_thumbnail_1">
            </p>
            <p class="control">
                <input class="input" type="text" placeholder="Image link" name="image_image_1">
            </p>
        </div>
    </div>
    <br />
    <a class="button is-success is-fullwidth" id="add-image">
        Add another image
    </a>
    <br />
    <p class="control">
        <button class="button is-primary" type="submit">Create project</button>
    </p>
</form>
</@layout.admin>