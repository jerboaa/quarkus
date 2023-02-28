package io.quarkus.resteasy.common.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.nativeimage.RuntimeReinitializedClassBuildItem;

class RestProcessor {

    /**
     * ResourceCleaner contains java.lang.ref.Cleaner references which need to get
     * runtime re-initialized.
     */
    @BuildStep
    public RuntimeReinitializedClassBuildItem reinitResourceCleaner() {
        return new RuntimeReinitializedClassBuildItem(
                "org.jboss.resteasy.spi.ResourceCleaner");
    }

}
