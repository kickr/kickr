<#import 'match_macros.ftl' as match>

<h5 class="ui header">
  <a href="/match">Latest Matches</a>

  <a class="ui tiny label" href="/match/new">+</a>
</h5>

<div class="ui one column grid">
  <div class="column">
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
  </div>
</div>