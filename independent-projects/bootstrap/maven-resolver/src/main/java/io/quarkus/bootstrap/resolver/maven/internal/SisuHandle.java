package io.quarkus.bootstrap.resolver.maven.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.sisu.launch.Main;
import org.eclipse.sisu.space.BeanScanning;
import org.eclipse.sisu.wire.ParameterKeys;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.name.Names;

import io.quarkus.bootstrap.resolver.maven.BootstrapMavenContext;
import io.quarkus.bootstrap.resolver.maven.MavenModelBuilder;
import io.quarkus.bootstrap.resolver.maven.workspace.LocalWorkspace;

@Named
@Singleton
public class SisuHandle {

    /**
     * Method that "boots" Resolver using Sisu.
     */
    public static SisuHandle boot(BootstrapMavenContext context) {
        // System.setProperty("sisu.debug", "true");
        final Module app = Main.wire(BeanScanning.INDEX, new BooterModule(context));
        return Guice.createInjector(app).getInstance(SisuHandle.class);
    }

    /**
     * Boot module: binds ctx and system properties to Sisu properties.
     */
    public static class BooterModule implements Module {
        private final BootstrapMavenContext context;

        public BooterModule(BootstrapMavenContext context) {
            this.context = context;
        }

        @Override
        public void configure(final Binder binder) {
            binder.bind(ParameterKeys.PROPERTIES).toInstance(System.getProperties());
            binder.bind(BootstrapMavenContext.class).toInstance(context);

            // Maven internally does this, so just provide it (component is on classpath)
            binder.bind(SecDispatcher.class).annotatedWith(Names.named("maven")).to(DefaultSecDispatcher.class)
                    .in(Singleton.class);

            // Customizations
            LocalWorkspace localWorkspace = new LocalWorkspace();
            binder.bind(LocalWorkspace.class).toInstance(localWorkspace);
            binder.bind(ModelBuilder.class).toInstance(new MavenModelBuilder(context));
        }
    }

    // The instance begins here

    private final RepositorySystem repositorySystem;

    private final SettingsDecrypter settingsDecrypter;

    @Inject
    public SisuHandle(RepositorySystem repositorySystem, SettingsDecrypter settingsDecrypter) {
        this.repositorySystem = repositorySystem;
        this.settingsDecrypter = settingsDecrypter;
    }

    public RepositorySystem getRepositorySystem() {
        return repositorySystem;
    }

    public SettingsDecrypter getSettingsDecrypter() {
        return settingsDecrypter;
    }
}
