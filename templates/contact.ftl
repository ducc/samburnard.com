<#import "layout.ftl" as layout>
<#import "social_buttons.ftl" as buttons>
<@layout.layout
title="Contact"
>
<div class="columns">
    <div class="column is-one-quarter">
        <div class="box">
            <h1 style="margin-bottom: 10px;" class="title">Contact Me</h1>
            <div class="content">
                <p>${content}</p>
            </div>
        </div>
    </div>
    <div class="column">
         <div class="box">
             <div class="content">
                 <h2>Email</h2>
                 <form action="/contact" method="post">
                     <label class="label">Name</label>
                     <p class="control">
                         <input class="input" type="text" placeholder="Your name" name="name">
                     </p>
                     <label class="label">Subject</label>
                     <p class="control">
                         <input class="input" type="text" placeholder="Your subject" name="subject">
                     </p>
                     <label class="label">Email</label>
                     <p class="control">
                         <input class="input" type="text" placeholder="Your email address" name="email">
                     </p>
                     <label class="label">Message</label>
                     <p class="control">
                         <textarea class="textarea" placeholder="Your message" name="message"></textarea>
                     </p>
                     <p class="control">
                         <button class="button is-primary" type="submit">Submit</button>
                     </p>
                 </form>
             </div>
         </div>
        <div class="columns">
	        <@buttons.social_column />
        </div>
    </div>
</div>
</@layout.layout>