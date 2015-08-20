<#if layout>
<!DOCTYPE html>
<html>
<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properities -->
  <title>table soccer result tracker - kckr</title>

  <link rel="stylesheet" type="text/css" href="/assets/lib/semantic.css">
  <link rel="stylesheet" type="text/css" href="/assets/lib/nprogress.css">

  <link rel="stylesheet" type="text/css" href="/assets/site.css">
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
      <div class="right menu">
        <#if user??>
          <form class="item" method="post" action="/logout">
            <input type="submit" class="ui button" value="Logout">
          </form>
        <#else>
          <a class="item" href="/login">
            <div class="ui primary button">Login</div>
          </a>
        </#if>
      </div>
    </div>
  </div>

  <div class="ui main container" id="content">
    <!-- ... -->
</#if>