<#import "admin.ftl" as layout>
<@layout.admin
tab_name="Home Page"
>
<form action="/admin/home/carousel" method="post">
    <label class="label">List your carousel images (urls separated with a comma)</label>
    <p class="control">
        <input class="input" type="text" placeholder="http://a.co/2f3.jpg,http://13.uk/242.jpg" name="images" value="${images}">
    </p>
    <p class="control">
        <button class="button is-primary" type="submit">Submit</button>
    </p>
</form>
</@layout.admin>