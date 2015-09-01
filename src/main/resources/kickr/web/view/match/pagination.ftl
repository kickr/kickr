<#macro pager page base="?">

<div class="ui small compact secondary menu">

  <#if page gt 2>
    <a class="item" href="${base}page=1">
      1
    </a>

    <div class="disabled item">
      ...
    </div>
  </#if>

  <#if page gt 1>
    <a class="item" href="${base}page=${page - 1}">
      ${page - 1}
    </a>
  </#if>

  <a class="item active" href="${base}page=${page}">
    ${page}
  </a>

  <a class="item" href="${base}page=${page + 1}">
    ${page + 1}
  </a>

  <a class="item" href="${base}page=${page + 2}">
    ...
  </a>
</div>

</#macro>