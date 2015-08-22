<#include "*/header.ftl">

<div class="ui breadcrumb">
  <a class="section" href="/">Home</a>
  <i class="right angle icon divider"></i>
  <div class="active section">Log In</div>
</div>

<#if redirectUri??>
  <div class="ui yellow message">
    <p>
      You need to be logged in to access the requested resource.
    </p>
  </div>
</#if>

<#include "login_form.ftl">

<#include "*/footer.ftl">