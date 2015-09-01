<#if layout>
<!DOCTYPE html>
<html>
<head>
  <!-- meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- props -->
  <title>table soccer result tracker - kckr</title>

  <!-- styles -->
  <link rel="stylesheet" type="text/css" href="/assets/lib/semantic.css">
  <link rel="stylesheet" type="text/css" href="/assets/lib/nprogress.css">

  <link rel="stylesheet" type="text/css" href="/assets/site.css">

  <!-- scripts -->
  <script src="/assets/lib/jquery.js"></script>
  <script src="/assets/lib/semantic.js"></script>
  <script src="/assets/lib/nprogress.js"></script>
  <script src="/assets/lib/jquery.pjax.js"></script>

  <script src="/assets/site.js"></script>
</head>
<body>

  <div class="ui borderless fixed menu">
    <div class="ui container">
      <a class="header item" href="/">
        <img class="logo" src="/assets/images/logo.png">
      </a>
      <a class="item" href="/match">Matches</a>
      <div class="item">
        <a class="ui green compact button" href="/match/new">+Match</a>
      </div>
      <div class="right account menu">
        <#if user??>

          <div class="ui item">

            <div class="ui floating top right pointing dropdown account icon button">
              <i class="user icon"></i>
              <div class="menu" tabindex="-1">
                <div class="header">Signed in as <strong>${user.name}</strong></div>
                <div class="divider"></div>
                <form class="item" method="post" action="/logout">
                  <div class="ui transparent input">
                    <input class="ui transparent input" type="submit" value="Log out">
                  </div>
                </form>
              </div>
            </div>
          </div>

          <script type="text/javascript">
            $('.account.icon').dropdown();
          </script>
        <#else>
          <a class="item" href="/login">
            <div class="ui primary button">Log in</div>
          </a>
        </#if>
      </div>
    </div>
  </div>

  <div class="ui main container" id="content">
    <!-- ... -->
</#if>