<#include "*/header.ftl">

<#import '../forms.ftl' as forms>

<div class="ui breadcrumb">
  <a class="section" href="/">Home</a>
  <i class="right angle icon divider"></i>
  <a class="section" href="/match">Matches</a>
  <i class="right angle icon divider"></i>
  <div class="active section">New</div>
</div>

<h1 class="ui header">Add a Match</h1>

<form class="ui new-match <@forms.formMarker form=form /> form" method="post" action="/match/new">

  <div class="ui top attached secondary segment">

    <div class="ui inline field">
      <label>Location</label>
      <span class="ui basic compact button">${(form.table.name)!}</span>
    </div>

  </div>

  <div class="ui top attached error message">
    <@forms.formErrors form=form />
  </div>

  <div class="ui attached segment">

    <input type="hidden" name="table.id" value="${(form.table.id)!}">
    <input type="hidden" name="table.name" value="${(form.table.name)!}">
    <input type="hidden" name="table.team1Alias" value="${(form.table.team1Alias)!}">
    <input type="hidden" name="table.team2Alias" value="${(form.table.team2Alias)!}">

    <div class="ui two column very relaxed stackable grid" style="position: relative">
      <div class="column">

        <div class="required <@forms.fieldMarker form=form field="team1.offense" /> field">
          <label>${(form.table.team1Alias)!}</label>
          <div class="ui fluid search" data-complete="/player/search?query={query}">
            <div class="ui input">
              <input
                class="prompt" type="text" name="team1.offense"
                value="${(form.team1.offense)!}" placeholder="Offense"
                autocomplete="off">
            </div>
            <div class="results"></div>
          </div>
        </div>
        <div class="field <@forms.fieldMarker form=form field="team1.defense" /> ">
          <div class="ui fluid search" data-complete="/player/search?query={query}">
            <div class="ui input">
              <input
                class="prompt" type="text" name="team1.defense"
                value="${(form.team1.defense)!}" placeholder="Defense"
                autocomplete="off">
            </div>
            <div class="results"></div>
          </div>
        </div>
      </div>
      <div class="ui vertical divider">
        vs.
      </div>
      <div class="column">
        <div class="required <@forms.fieldMarker form=form field="team2.offense" /> field">
          <label>${(form.table.team2Alias)!}</label>
          <div class="ui fluid search" data-complete="/player/search?query={query}">
            <div class="ui input">
              <input
                class="prompt" type="text" name="team2.offense"
                value="${(form.team2.offense)!}" placeholder="Offense"
                autocomplete="off">
            </div>
            <div class="results"></div>
          </div>
        </div>
        <div class="<@forms.fieldMarker form=form field="team2.defense" /> field">
          <div class="ui fluid search" data-complete="/player/search?query={query}">
            <div class="ui input">
              <input
                class="prompt" type="text" name="team2.defense"
                value="${(form.team2.defense)!}" placeholder="Defense"
                autocomplete="off">
            </div>
            <div class="results"></div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="ui attached segment">

    <div class="required <@forms.fieldMarker form=form field="games" /> field">
      <label>Game Results</label>
      <input
        type="text" name="games" style="max-width: 250px"
        value="${(form.games)!}"
        placeholder="e.g. 1:3 4:5">

      <p class="note">
        Specify as space separated list of games.
      </p>
    </div>

  </div>

  <div class="ui attached segment">

    <div class="<@forms.fieldMarker form=form field="played" /> field">
      <label>Date/Time</label>

      <div class="ui right action input" style="max-width: 350px" data-datepicker>
        <input type="text" name="played" value="${(form.played)!}" placeholder="2012-05-11 17:00">
        <button class="ui icon button">
          <i class="calendar icon"></i>
        </button>
      </div>

      <p class="note">
        Defaults to now, if left empty.
      </p>
    </div>

  </div>

  <div class="ui attached segment">
    <input type="submit" class="ui primary button" value="Create Match">
  </div>

</form>

<script type="text/javascript">
  $('.new-match.form').form({
    fields: {
      'team2.offense': {
        identifier  : 'team1.offense',
        rules: [
          {
            type : 'empty',
            prompt : 'Please enter a Player'
          }
        ]
      },
      'team1.defense': {
        identifier  : 'team2.offense',
        rules: [
          {
            type : 'empty',
            prompt : 'Please enter a Player'
          }
        ]
      },
      matches: {
        identifier : 'matches',
        rules: [
          {
            type : 'regExp[/^([\\d]+:[\\d]+\\s?)+$/]',
            prompt : 'Please enter at least one match'
          }
        ]
      },
      played: {
        identifier : 'played',
        optional: true,
        rules: [
          {
            type : 'regExp[/^[\\d]{4}-[\\d]{2}-[\\d]{2}( [\\d]{2}:[\\d]{2})?$/]',
            prompt : 'Please specify date/time in YYYY-mm-dd HH:MM format'
          }
        ]
      }
    }
  });
</script>

<#include "*/footer.ftl">