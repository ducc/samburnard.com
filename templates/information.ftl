<#import "layout.ftl" as layout>
<#import "social_buttons.ftl" as buttons>
<@layout.layout
title="Information"
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
                    <div class="content">
                        <p>Your about me content</p>
                    </div>
                </div>
            </div>
            <div class="column is-6">
                <div class="box">
                    <h1 style="margin-bottom: 10px;" class="title">Contact Me</h1>
                    <div class="content">
                        <p>Your contact me content</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="columns">
		    <@buttons.social_column />
        </div>
    </div>
</div>
</@layout.layout>