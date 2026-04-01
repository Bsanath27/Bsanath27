package com.skyride.feature.rideselection;

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
public final class RideSelectionViewModel_Factory implements Factory<RideSelectionViewModel> {
  @Override
  public RideSelectionViewModel get() {
    return newInstance();
  }

  public static RideSelectionViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RideSelectionViewModel newInstance() {
    return new RideSelectionViewModel();
  }

  private static final class InstanceHolder {
    private static final RideSelectionViewModel_Factory INSTANCE = new RideSelectionViewModel_Factory();
  }
}
