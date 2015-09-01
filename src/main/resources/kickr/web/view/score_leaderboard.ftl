<#-- @ftlvariable name="" type="kickr.web.view.ScoreLeaderboardView" -->

<div class="ui two column grid">

  <div class="column">

    <h5 class="ui header">
      Global Leader Board
    </h5>

    <div class="ui top attached segment">
      <table class="ui very basic compact highscore table">
        <tbody>
          <#list data.scores as score>
            <tr>
              <td class="collapsing">#${score?counter}</td>
              <td class="player"><strong>${score.player.alias}</td>
              <td class="right aligned collapsing">${score.value}</td>
              <td class="right aligned collapsing" title="changes last 7 days">
                (+${score.added})
              </td>
            </tr>
          <#else>
            <tr>
              <td>No scores recorded yet</td>
            </tr>
          </#list>
        </tbody>
      </table>
    </div>

  </div>

  <div class="column">

    <h5 class="ui header">
      Player Performance
    </h5>

    <div class="ui top attached segment">
      <table class="ui very basic compact highscore table">
        <tbody>
          <#list data.performance as p>
            <tr>
              <td class="collapsing">#${p?counter}</td>
              <td class="player"><strong>${p.player.alias}</td>
              <td class="right aligned collapsing">${p.rating}</td>
              <td class="right aligned collapsing" title="average ${p.average}, confidence ${p.confidence}">
                *
              </td>
            </tr>
          <#else>
            <tr>
              <td>No performance recorded yet.</td>
            </tr>
          </#list>
        </tbody>
      </table>
    </div>

  </div>
</div>