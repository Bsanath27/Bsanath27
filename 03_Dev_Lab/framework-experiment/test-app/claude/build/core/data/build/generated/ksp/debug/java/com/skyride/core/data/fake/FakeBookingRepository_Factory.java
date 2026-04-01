package com.skyride.core.data.fake;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class FakeBookingRepository_Factory implements Factory<FakeBookingRepository> {
  @Override
  public FakeBookingRepository get() {
    return newInstance();
  }

  public static FakeBookingRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FakeBookingRepository newInstance() {
    return new FakeBookingRepository();
  }

  private static final class InstanceHolder {
    private static final FakeBookingRepository_Factory INSTANCE = new FakeBookingRepository_Factory();
  }
}
