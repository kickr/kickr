<#include "*/header.ftl">

<div class="ui breadcrumb">
  <a class="section" href="/">Home</a>
  <i class="right angle icon divider"></i>
  <a class="section" href="/match">Matches</a>
  <i class="right angle icon divider"></i>
  <span class="active section">${match.id?c}</span>
</div>


<div class="ui secondary menu">
  <h1 class="ui left floated header">
    Match ${match.id?c}
  </h1>

  <div class="right menu">

    <div class="fitted item">

      <div class="ui compact basic buttons">
        <a class="ui button" href="/match/${match.id?c}/edit">Edit</a>

        <#if !match.rated>
          <div class="ui floating edit dropdown icon compact button" tabindex="0">
            <i class="dropdown icon"></i>
            <div class="menu" tabindex="-1">
              <a class="item" href="#">Mark as cheated</a>
            </div>
          </div>
        </#if>
      </div>

      <script type="text/javascript">
        $('.edit.dropdown').dropdown();
      </script>
    </div>
  </div>
</div>

<div class="ui top attached tools secondary segment">

  <div class="ui two column grid">
    <div class="ui left floated ten wide column">
      Played <strong>${match.played?date}</strong> at <strong>${table.name}</strong>
    </div>

    <div class="right floated right aligned six wide column">
      <#if match.rated>
        <div class="ui green label">
          Rated
        </div>
      </#if>

      <#if match.removed>
        <div class="ui red label">
          Removed
        </div>
      </#if>
    </div>
  </div>
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
            <#if match.team1.defense.alias != match.team1.offense.alias>
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
            <#if match.team2.defense.alias != match.team2.offense.alias>
              <p>
                ${match.team2.defense.alias}
              </p>
            </#if>
          </div>
        </div>
      </div>
    </div>

    <div class="column">
      <span class="ui huge header">
        ${match.result.team1}:${match.result.team2}
      </span>
    </div>

  </div>

</div>

<div class="ui history feed">
  <div class="event">
    <div class="label">
      <i class="add circle icon"></i>
    </div>
    <div class="content">
      <div class="summary">
        <a href class="user">Nikku</a> filed this game
        <div class="date">5 hours ago</div>
      </div>
    </div>
  </div>

  <div class="event">
    <div class="label">
      <i class="selected radio icon"></i>
    </div>
    <div class="content">
      <div class="summary">
        <strong class="user">kckr</strong> rated this game
        <div class="date">3 hours ago</div>
      </div>
    </div>
  </div>
</div>

<#include "*/footer.ftl">