<#import "layout.ftl" as layout>
<@layout.layout
title="Login"
>
<h1 class="title">Login</h1>
<h2 class="subtitle">Please login to your account.</h2>
<div class="box">
    <form action="/auth/login" method="post">
        <label class="label">Username</label>
        <p class="control">
            <input class="input" type="text" placeholder="Your username" name="username">
        </p>
        <label class="label">Password</label>
        <p class="control">
            <input class="input" type="password" placeholder="Your password" name="password">
        </p>
        <p class="control">
            <button class="button is-primary" type="submit">Login</button>
        </p>
    </form>
</div>
</@layout.layout>