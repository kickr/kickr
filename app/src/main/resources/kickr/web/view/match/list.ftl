<#import 'match_macros.ftl' as match>
<#import 'pagination.ftl' as pagination>

<#include "*/header.ftl">

<div class="ui breadcrumb">
  <a class="section" href="/">Home</a>
  <i class="right angle icon divider"></i>
  <span class="active section">Matches</span>
</div>


<h1 class="ui header">Recorded Matches</h1>

<div class="ui secondary menu">
  <a class="<#if (filter!"latest") == "latest">active</#if> item" href="?filter=latest">
    Latest
  </a>
  <a class="<#if (filter!"latest") == "your">active</#if> item" href="?filter=your">
    Your
  </a>
  <div class="right menu">
    <div class="item">
      <form>
        <div class="ui icon input">
          <#if filter??>
            <input type="hidden" name="filter" value="${filter}">
          </#if>
          <input type="text" name="search" value="${search!""}" placeholder="Search...">
          <i class="search link icon"></i>
        </div>
      </form>
    </div>
    <div class="horizontally fitted item">
      <a class="ui green compact button" href="/match/new">+Match</a>
    </div>
  </div>
</div>


<div class="ui top attached segment">
  <table class="ui very basic compact matches table">
    <tbody>
      <#list matches as m>
        <@match.matchRow match=m />
      <#else>
        <tr>
          <td>No recorded matches. Go play!</td>
        </tr>
      </#list>
    </tbody>
  </table>
</div>

<#assign base="?">

<#if filter??>
  <#if base != "?">
    <#assign base="${base}&">
  </#if>

  <#assign base="${base}filter=${filter}">
</#if>

<#if search??>
  <#if base != "?">
    <#assign base="${base}&">
  </#if>

  <#assign base="${base}search=${search}">
</#if>

<#if base != "?">
  <#assign base="${base}&">
</#if>

<div class="ui center aligned basic segment">
  <@pagination.pager page=page base=base />
</div>


<#include "*/footer.ftl">