<#include "*/header.ftl">

<div class="ui error message">
  <div class="header">
    Could not complete due to validation errors:
  </div>
  <ul>
    <#list violations as error>
      <li><strong>${error.propertyPath}</strong>: ${error.message}</li>
    </#list>
  </ul>
</div>

<p>
  <a href="javascript:window.history.back()">Go back</a>, fix the issues and try again.
</p>

<#include "*/footer.ftl">