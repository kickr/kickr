<#macro players team>
  ${team.offense.alias}<#if team.offense.alias != team.defense.alias>,${team.defense.alias}</#if>
</#macro>

<#macro matchRow match>
  <tr class="${match.rated?string('', 'unrated')} ${match.removed?string('removed', '')}">
    <td class="collapsing"><a href="/match/${match.id?c}">${match.id?c}</a></td>
    <td class="collapsing players">
      <@players team=match.team1 />
    </td>
    <td class="collapsing">
      <strong title="${match.table.team1Alias!}">
        ${match.result.team1}
      </strong>
      :
      <strong title="${match.table.team2Alias!}">
        ${match.result.team2}
      </strong>
    </td>
    <td class="collapsing players">
      <@players team=match.team2 />
    </td>
    <td class="right aligned">
      ${match.played?date}<#if match.table.name??>, ${match.table.name}</#if>
    </td>
  </tr>
</#macro>