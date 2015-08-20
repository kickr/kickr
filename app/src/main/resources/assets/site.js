/* kickr client site support */

$(function() {

  function applyDirectives() {
    $('[data-fetch]').each(function() {

      var $el = $(this);

      $el.load($el.attr('data-fetch'), function() {
        $el.removeClass('loading').addClass('loaded');
      });
    });
  }

  NProgress.configure({ showSpinner: false });

  var ajaxStart = NProgress.start.bind(NProgress),
      ajaxStop = NProgress.done.bind(NProgress);

  $(document).ajaxStart(ajaxStart);

  $(document).ajaxStop(ajaxStop);

  $(document).pjax('a', '#content');

  $(document).on('pjax:send', ajaxStart);
  $(document).on('pjax:complete', ajaxStop);

  $(document).on('pjax:end', applyDirectives);


  // initialize (!)
  applyDirectives();
});