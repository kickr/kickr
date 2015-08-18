/* kickr client site support */

$(function() {

  NProgress.configure({ showSpinner: false });

  var ajaxStart = NProgress.start.bind(NProgress),
      ajaxStop = NProgress.done.bind(NProgress);

  $(document).ajaxStart(ajaxStart);

  $(document).ajaxStop(ajaxStop);

  $(document).pjax('a', '#content');

  $(document).on('pjax:start', ajaxStart);
  $(document).on('pjax:end', ajaxStop);

  $('[data-fetch]').each(function() {

    var $el = $(this);

    $el.load($el.attr('data-fetch'), function() {
      $el.removeClass('loading').addClass('loaded');
    });
  });

});