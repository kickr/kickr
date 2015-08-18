<#-- @ftlvariable name="" type="kickr.web.view.LatestMatchesView" -->

<#import 'match.ftl' as match>

<div class="ui one column grid">
  <div class="column">
    <h5 class="ui top attached header">
      Latest Matches
    </h5>
    <div class="ui basic bottom attached segment">
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
        <a href="/matches">Show All</a> or
        <a href="/matches/new">File new Match</a>
      </p>
    </div>
  </div>
</div>