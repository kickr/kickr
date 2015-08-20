<#import 'match_macros.ftl' as match>

<#include "*/header.ftl">

<div class="ui breadcrumb">
  <a class="section" href="/">Home</a>
  <i class="right angle icon divider"></i>
  <span class="active section">Matches</span>
</div>


<h1 class="ui header">Recorded Matches</h1>

<div class="ui secondary menu">
  <a class="active item">
    Latest
  </a>
  <a class="item">
    Your
  </a>
  <div class="right menu">
    <div class="item">
      <div class="ui icon input">
        <input type="text" placeholder="Search...">
        <i class="search link icon"></i>
      </div>
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
        <@match.row match=m />
      <#else>
        <tr>
          <td>No recorded matches. Go play!</td>
        </tr>
      </#list>
    </tbody>
  </table>
</div>

<div class="ui center aligned basic segment">
  <div class="ui compact small secondary menu">
    <a class="active item">
      1
    </a>
    <div class="disabled item">
      ...
    </div>
    <a class="item">
      10
    </a>
    <a class="item">
      11
    </a>
    <a class="item">
      12
    </a>
  </div>
</div>


<#include "*/footer.ftl">