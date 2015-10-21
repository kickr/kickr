<div class="ui top attached segment">
  <div id="chart_container">
    <div id="y_axis"></div>
    <div id="chart"></div>
    <div id="legend"></div>
  </div>
</div>

<script>

  function createGraph(data) {

    var $chartElement = document.querySelector("#chart");

    var width = $chartElement.parentNode.parentNode.clientWidth - 180;

    var palette = new Rickshaw.Color.Palette();

    var allSeries = data.series.map(function(s) {

      return {
        name: s.element.alias,
        color: palette.color(),
        data: s.results.map(function(r) {
          return { x: r.date / 1000, y: r.value };
        })
      };
    });

    var graph = new Rickshaw.Graph( {
      element: $chartElement,
      renderer: 'line',
      width: width,
      height: 300,
      series: allSeries,
      min: 'auto',
      padding: { top: 0.2, bottom: 0.1 }
    });

    var hoverDetail = new Rickshaw.Graph.HoverDetail( {
        graph: graph
    } );

    var x_axis = new Rickshaw.Graph.Axis.Time({
      graph: graph,
      timeUnit: (new Rickshaw.Fixtures.Time()).unit('month')
	});

    var y_axis = new Rickshaw.Graph.Axis.Y({
        graph: graph,
        orientation: 'left',
        tickFormat: Rickshaw.Fixtures.Number.formatKMBT,
        element: document.querySelector('#y_axis'),
    });

    var legend = new Rickshaw.Graph.Legend( {
      element: document.querySelector('#legend'),
      graph: graph
    });

    graph.render();

  }

  $.getJSON('/graphs/timeline?month=12', function(json) {
    createGraph(json);
  });
</script>
