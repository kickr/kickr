<#include "*/header.ftl">

<#if redirectUri??>
  <div class="ui yellow message">
    <p>
      Please login in order to access the requested resource.
    </p>
  </div>
</#if>

<#include "login_form.ftl">

<#include "*/footer.ftl">