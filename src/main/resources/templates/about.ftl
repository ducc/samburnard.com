<#macro social icon="" link="">
<div class="column is-one-third">
    <a href="${link}">
        <div class="box has-text-centered">
            <div class="content">
                <p>
                    <i class="fa ${icon}"></i>
                </p>
            </div>
        </div>
    </a>
</div>
</#macro>

<#import "layout.ftl" as layout>
<@layout.layout
title="About"
>
<br />
<br />
<br />
<div class="columns">
    <div class="column is-10 is-offset-1">
        <div class="columns">
            <div class="column is-6">
                <div class="box">
                    <center style="margin-bottom: 10px;"><h1 class="title">About Me</h1></center>
                    <p>${content}</p>
                </div>
                <div class="columns">
                    <@social "fa-twitter" "#" />
                    <@social "fa-instagram" "https://instagram.com/sam_burnard" />
                    <@social "fa-facebook" "#" />
                </div>
            </div>
            <div class="column is-6">
                <div class="box">
                    <iframe src="//lightwidget.com/widgets/51715ee9995a543ab9c9253d5243220d.html" id="lightwidget_51715ee999" name="lightwidget_51715ee999"  scrolling="no" allowtransparency="true" class="lightwidget-widget" style="width: 100%; border: 0; overflow: hidden;"></iframe>
                    <p style="font-size: 12px; text-align: right;">
                        <a href="https://instagram.com/sam_burnard">@sam_burnard</a> on instagram
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>
</@layout.layout>