<#include "*/header.ftl">

<div class="ui breadcrumb">
  <a class="section" href="/">Home</a>
  <i class="right angle icon divider"></i>
  <span class="active section">Graphs</span>
</div>

<h1 class="ui header">Graphs</h1>

<div class="ui secondary menu">
  <a class="<#if tab == "scores">active</#if> item" href="?tab=scores">
    Scores
  </a>
  <a class="<#if tab == "performance">active</#if> item" href="?tab=performance">
    Performance
  </a>
  <a class="<#if tab == "matches">active</#if> item" href="?tab=matches">
    Matches
  </a>

  <div class="right menu">
    <div class="horizontally fitted item">
      <form>
        <div class="ui icon input">
          <input type="hidden" name="tab" value="${tab}">

          <input type="text" name="filter" value="${filter!""}" placeholder="Filter...">
          <i class="filter icon"></i>
        </div>
      </form>
    </div>
  </div>
</div>

<#include "${tab}_tab.ftl">

<style>
  #chart_container {
    margin: 10px 0;
    position: relative;
    display: inline-block;
    font-family: Arial, Helvetica, sans-serif;
  }
  #chart {
    display: inline-block;
    margin-left: 40px;
  }
  #y_axis {
    position: absolute;
    top: 0;
    bottom: 0;
    width: 40px;
  }
  #legend {
    display: inline-block;
    vertical-align: top;
    margin: 0 0 0 10px;
  }
</style>

<!-- timeline styles -->
<link rel="stylesheet" type="text/css" href="/assets/lib/analytics/rickshaw.min.css">

<!-- timeline scripts -->
<script src="/assets/lib/analytics/d3.min.js"></script>
<script src="/assets/lib/analytics/rickshaw.min.js"></script>

<#include "*/footer.ftl">