<#include "*/header.ftl">

<div class="ui breadcrumb">
  <a class="section" href="/">Home</a>
  <i class="right angle icon divider"></i>
  <a class="section" href="/match">Matches</a>
  <i class="right angle icon divider"></i>
  <div class="active section">New</div>
</div>


<h1 class="ui header">Add a Match</h1>

<form class="ui form" method="post" action="/match/new">

  <div class="ui top attached secondary segment">

    <div class="ui inline field">
      <label>Location</label>
      <button class="ui basic compact button">Camunda HQ</button>
    </div>

  </div>

  <div class="ui attached segment">

    <div class="ui two column very relaxed stackable grid" style="position: relative">
      <div class="column">

        <div class="required field">
          <label>Kaffee</label>
          <input type="text" name="team1.offense" placeholder="Offense">
        </div>
        <div class="field">
          <input type="text" name="team1.defense" placeholder="Defense">
        </div>
      </div>
      <div class="ui vertical divider">
        vs.
      </div>
      <div class="column">
        <div class="required field">
          <label>Klo</label>
          <input type="text" name="team2.offense" placeholder="Offense">
        </div>
        <div class="field">
          <input type="text" name="team2.defense" placeholder="Defense">
        </div>
      </div>
    </div>
  </div>

  <div class="ui attached segment">

    <div class="required field">
      <label>Game Results</label>
      <input type="text" name="matches" placeholder="e.g. 1:3 4:5" style="max-width: 250px">

      <p class="note">
        Specify as space separated list of games.
      </p>
    </div>

  </div>

  <div class="ui attached segment">

    <div class="field">
      <label>Date/Time</label>

      <div class="ui right action input" style="max-width: 350px">
        <input type="text" name="played" placeholder="2012-05-11 17:00">
        <button class="ui icon button">
          <i class="calendar icon"></i>
        </button>
      </div>

      <p class="note">
        Defaults to now, unless specified.
      </p>
    </div>

  </div>

  <div class="ui attached segment">
    <input type="submit" class="ui primary button" value="Save Game">
  </div>

</form>
<#include "*/footer.ftl">