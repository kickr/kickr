<#include "*/header.ftl">

<div class="ui icon yellow message">
  <i class="lock icon"></i>
  <div class="content">
    <div class="header">
      Login required
    </div>
    <p>You need to be logged in to access this resource.</p>
  </div>
</div>

<div class="ui middle aligned center aligned login grid">
  <div class="column">
    <h3 class="ui header">
      <div class="content">
        Log-in to Kckr
      </div>
    </h3>
    <form class="ui large form" action="/user/login" method="post">
      <div class="ui segment">
        <div class="field">
          <div class="ui left icon input">
            <i class="user icon"></i>
            <input type="text" name="email" placeholder="E-mail address">
          </div>
        </div>
        <div class="field">
          <div class="ui left icon input">
            <i class="lock icon"></i>
            <input type="password" name="password" placeholder="Password">
          </div>
        </div>
        <div class="ui fluid large submit button">Login</div>
      </div>

      <div class="ui error message"></div>

    </form>

    <div class="ui message">
      New to us? <a href="/user/signup">Sign Up</a>
    </div>
  </div>
</div>

<#include "*/footer.ftl">