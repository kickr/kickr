<#macro players team>
  ${team.offense.alias}<#if team.offense.alias != team.defense.alias>,${team.defense.alias}</#if>
</#macro>

<#macro matchRow match>
  <tr class="${match.rated?string('', 'unrated')} ${match.removed?string('removed', '')}">
    <td class="collapsing"><a href="/match/${match.id}">${match.id}</a></td>
    <td class="collapsing players">
      <@players team=match.teams.team1 />
    </td>
    <td class="collapsing">
      <strong title="${match.table.team1Alias!}">
        ${match.result.team1}
      </strong>
      :
      <strong title="$h.table.team2Alias!}">
        ${match.result.team2}
      </strong>
    </td>
    <td class="collapsing players">
      <@players team=match.teams.team2 />
    </td>
    <td class="right aligned">
      ${match.played?date}<#if match.table.name??>, ${match.table.name}</#if>
    </td>
  </tr>
</#macro>