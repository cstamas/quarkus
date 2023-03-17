/**
 *
 */
package io.quarkus.bootstrap.resolver.maven;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.quarkus.bootstrap.resolver.maven.internal.SisuHandle;

/**
 * Initializes {@link BootstrapMavenContext}, to be used when you are inside of Maven (ie. plugin).
 */
@Singleton
@Named
public class MavenArtifactResolverInitializer {

    public MavenArtifactResolverInitializer() {
        reset();
    }

    @Inject
    public MavenArtifactResolverInitializer(SisuHandle sisuHandle) {
        BootstrapMavenContext.sisuBooter.set(sisuHandle);
    }

    public static void reset() {
        BootstrapMavenContext.sisuBooter.set(null);
    }
}
