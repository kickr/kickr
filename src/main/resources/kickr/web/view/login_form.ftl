<div class="ui middle aligned center aligned login grid">
  <div class="column">
    <h3 class="ui header">
      Enter kckr
    </h3>
    <form class="ui form ${(errors?size > 0)?string('error', '')}" action="/login" method="post">

      <div class="ui error message">
        <#list errors as error>
          <p>${error.content}</p>
        </#list>
      </div>

      <div class="ui segment">
        <div class="field">
          <div class="ui left icon input">
            <i class="user icon"></i>
            <input type="text" name="name" placeholder="Username" autofocus>
          </div>
        </div>
        <div class="field">
          <div class="ui left icon input">
            <i class="lock icon"></i>
            <input type="password" name="password" placeholder="Password">
          </div>
        </div>
        <input type="submit" class="ui fluid large blue submit button" value="Sign in" />
        <#if redirectUri??>
          <input type="hidden" name="redirectTo" value="${redirectUri}">
        </#if>
      </div>

    </form>

    <div class="ui message">
      No account yet? <a href="/user/signup">Sign up</a> instead.
    </div>
  </div>
</div>