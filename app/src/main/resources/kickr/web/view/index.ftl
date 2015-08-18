<#include "*/header.ftl">

<div class="ui two column grid">
  <div class="column">
    <pre>
          da great. jup

    ██╗  ██╗██╗ ██████╗██╗  ██╗██████╗
    ██║ ██╔╝██║██╔════╝██║ ██╔╝██╔══██╗
    █████╔╝ ██║██║     █████╔╝ ██████╔╝
    ██╔═██╗ ██║██║     ██╔═██╗ ██╔══██╗
    ██║  ██╗██║╚██████╗██║  ██╗██║  ██║
    ╚═╝  ╚═╝╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝</pre>
  </div>

  <div class="column">
    <div class="ui basic loading segment" data-fetch="/matches/latest">
      <#include "matches_latest_dummy.ftl">
    </div>
  </div>
</div>

<div class="ui horizontal section divider">Stats</div>

<div class="ui basic loading segment" data-fetch="/score/leaderboard">
  <#include "score_leaderboard_dummy.ftl">
</div>

<#include "*/footer.ftl">