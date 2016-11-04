<#macro admin
tab_name="Default Tab Name"
>
<#import "../layout.ftl" as layout>
<@layout.layout
title="Admin"
>
<div class="columns">
    <div class="column is-one-quarter">
        <#include "admin_nav.ftl" />
    </div>
    <div class="column">
        <div class="box">
            <h2 class="title">${tab_name}</h2>
            <#nested />
        </div>
    </div>
</div>
</@layout.layout>
</#macro>