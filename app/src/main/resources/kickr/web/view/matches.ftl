<#import 'match.ftl' as match>

<#include "*/header.ftl">

<div class="ui breadcrumb">
  <a class="section" href="/">Home</a>
  <i class="right angle icon divider"></i>
  <span class="active section">Matches</span>
</div>


<h1 class="ui header">Latest Matches</h1>

<table class="ui very basic compact matches table">
  <tbody>
    <#list matches as m>
      <@match.row match=m />
    <#else>
      <tr>
        <td>No recorded matches. Go play!</td>
      </tr>
    </#list>
  </tbody>
</table>

<p class="right aligned">
  <a href="/matches/new">File new Match</a>
</p>


<#include "*/footer.ftl">