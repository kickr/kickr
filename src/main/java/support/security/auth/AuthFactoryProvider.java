package support.security.auth;

import java.security.Principal;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import support.security.annotation.Auth;

/**
 *
 * @author nikku
 */
@Singleton
class AuthFactoryProvider<P extends Principal> extends AbstractValueFactoryProvider {
  
  private final AuthFactory<P> factory;

  @Inject
  public AuthFactoryProvider(final MultivaluedParameterExtractorProvider extractorProvider,
                 final AuthFactory<P> factory,
                 final ServiceLocator injector) {
    super(extractorProvider, injector, Parameter.Source.UNKNOWN);
    this.factory = factory;
  }

  @Override
  protected Factory<?> createValueFactory(final Parameter parameter) {
    final Class<?> classType = parameter.getRawType();
    final Auth auth = parameter.getAnnotation(Auth.class);

    if (auth != null && classType.isAssignableFrom(factory.getPrincipalClass())) {
      return factory;
    } else {
      return null;
    }
  }

  public static class AuthInjectionResolver extends ParamInjectionResolver<Auth> {
    public AuthInjectionResolver() {
      super(AuthFactoryProvider.class);
    }
  }

  public static class Binder<P extends Principal> extends AbstractBinder {

    private final AuthFactory<P> factory;

    public Binder(AuthFactory<P> factory) {
      this.factory = factory;
    }

    @Override
    protected void configure() {
      bind(this.factory).to(AuthFactory.class);
      bind(AuthFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
      bind(AuthInjectionResolver.class).to(
        new TypeLiteral<InjectionResolver<Auth>>() { }
      ).in(Singleton.class);
    }
  }
}

