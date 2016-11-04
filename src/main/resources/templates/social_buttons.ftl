<#macro social icon="" link="">
<div class="column">
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

<#macro social_column>
	<#if twitter?has_content>
		<@buttons.social "fa-twitter" "https://twitter.com/${twitter}" />
	</#if>
	<#if instagram?has_content>
		<@buttons.social "fa-instagram" "https://instagram.com/${instagram}" />
	</#if>
	<#if facebook?has_content>
		<@buttons.social "fa-facebook" "${facebook}" />
	</#if>
	<#if youtube?has_content>
		<@buttons.social "fa-youtube" "${youtube}" />
	</#if>
	<#if behance?has_content>
		<@buttons.social "fa-behance" "${behance}" />
	</#if>
	<#if imgur?has_content>
		<@buttons.social "fa-imgur" "${imgur}" />
	</#if>
</#macro>