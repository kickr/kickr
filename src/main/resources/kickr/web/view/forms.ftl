<#macro formMarker form><#if form.errors??>error</#if></#macro>

<#macro fieldMarker form field><#if form.errors??><#list form.errors as error><#if error.propertyPath == field>error</#if></#list></#if></#macro>

<#macro formErrors form>
  <#if form.errors??>
    <ul class="list">
      <#list form.errors as error>
        <li>${error.propertyPath}: ${error.message}</li>
      </#list>
    </ul>
  </#if>
</#macro>
