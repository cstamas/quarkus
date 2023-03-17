package io.quarkus.bootstrap.resolver.maven;

import org.eclipse.aether.util.version.GenericVersionScheme;
import org.eclipse.aether.version.InvalidVersionSpecificationException;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.version.VersionConstraint;
import org.eclipse.aether.version.VersionScheme;

public final class VersionHelper {
    private static final VersionScheme genericVersionScheme = new GenericVersionScheme();

    public static Version newVersion(String version) {
        try {
            return genericVersionScheme.parseVersion(version);
        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException(e);
        }
    }

    public static VersionConstraint newVersionConstraint(String version) {
        try {
            return genericVersionScheme.parseVersionConstraint(version);
        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException(e);
        }
    }
}
