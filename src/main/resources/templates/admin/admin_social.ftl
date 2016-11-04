<#import "admin.ftl" as layout>
<@layout.admin
tab_name="Social Media Profiles"
>
<form action="/admin/social" method="post">
    <label class="label">Instagram username</label>
    <p class="control">
        <input class="input" type="text" placeholder="coolguy18" name="instagram" <#if instagram?has_content>value="${instagram}"</#if>>
    </p>
    <label class="label">Twitter username</label>
    <p class="control">
        <input class="input" type="text" placeholder="coolguy12" name="twitter" <#if twitter?has_content>value="${twitter}"</#if>>
    </p>
    <label class="label">Facebook page (link)</label>
    <p class="control">
        <input class="input" type="text" placeholder="https://www.facebook.com/PageName/" name="facebook" <#if facebook?has_content>value="${facebook}"</#if>>
    </p>
    <label class="label">Youtube channel (link)</label>
    <p class="control">
        <input class="input" type="text" placeholder="https://www.youtube.com/username" name="youtube" <#if youtube?has_content>value="${youtube}"</#if>>
    </p>
    <label class="label">Behance profile (link)</label>
    <p class="control">
        <input class="input" type="text" placeholder="https://www.behance.net/myusername" name="behance" <#if behance?has_content>value="${behance}"</#if>>
    </p>
    <label class="label">Imgur profile (link)</label>
    <p class="control">
        <input class="input" type="text" placeholder="https://www.imgur.com/" name="imgur" <#if imgur?has_content>value="${imgur}"</#if>>
    </p>
    <p class="control">
        <button class="button is-primary" type="submit">Submit</button>
    </p>
</form>
</@layout.admin>