<#include "*/header.ftl">

<div class="ui breadcrumb">
  <a class="section" href="/">Home</a>
  <i class="right angle icon divider"></i>
  <a class="section" href="/match">Matches</a>
  <i class="right angle icon divider"></i>
  <span class="active section">${match.id}</span>
</div>


<h1 class="ui header">
  Match ${match.id}

  <div class="ui icon buttons">
    <button class="ui button"><i class="bold icon"></i></button>
    <button class="ui button"><i class="underline icon"></i></button>
    <button class="ui button"><i class="text width icon"></i></button>
  </div>
</h1>

<div class="ui top attached secondary segment">
  Played <strong>${match.played?date}</strong> at <strong>${table.name}</strong>.
</div>

<div class="ui bottom attached segment">

  <div class="ui two column center aligned middle aligned divided stackable grid">

    <div class="column">
      <div class="ui two column very relaxed stackable grid" style="position: relative">

        <div class="row">
          <div class="column">
            <h5>${(table.team1Alias)!}</h5>
            <p>
              ${match.team1.offense.alias}
            </p>
            <#if match.team1.defense??>
              <p>
                ${match.team1.defense.alias}
              </p>
            </#if>
          </div>

          <div class="ui vertical divider">
            vs.
          </div>


          <div class="center aligned column">
            <h5>${(table.team2Alias)!}</h5>
            <p>
              ${match.team2.offense.alias}
            </p>
            <#if match.team2.defense??>
              <p>
                ${match.team2.defense.alias}
              </p>
            </#if>
          </div>
        </div>
      </div>
    </div>

    <div class="column">
      <h2>
        ${match.result.team1}:${match.result.team2}
      </h2>
    </div>

  </div>

</div>

<#if match.rated>
  <div class="ui green segment">
    Match was rated.
  </div>
</#if>

<#if match.removed>
  <div class="ui secondary segment">
    Marked was removed.
  </div>
</#if>

<#include "*/footer.ftl">