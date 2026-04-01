package com.skyride.feature.pricing;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class PricingViewModel_Factory implements Factory<PricingViewModel> {
  @Override
  public PricingViewModel get() {
    return newInstance();
  }

  public static PricingViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PricingViewModel newInstance() {
    return new PricingViewModel();
  }

  private static final class InstanceHolder {
    private static final PricingViewModel_Factory INSTANCE = new PricingViewModel_Factory();
  }
}
