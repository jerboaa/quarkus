package io.quarkus.resteasy.common.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedClassBuildItem;

class RestProcessor {

    /**
     * ResourceCleaner contains java.lang.ref.Cleaner references which need to get
     * runtime initialized.
     */
    @BuildStep
    public RuntimeInitializedClassBuildItem runtimeInitResourceCleaner() {
        return new RuntimeInitializedClassBuildItem(
                "org.jboss.resteasy.spi.ResourceCleaner");
    }

}
