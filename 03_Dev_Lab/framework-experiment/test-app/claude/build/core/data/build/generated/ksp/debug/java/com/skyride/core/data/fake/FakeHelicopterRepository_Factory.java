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
public final class FakeHelicopterRepository_Factory implements Factory<FakeHelicopterRepository> {
  @Override
  public FakeHelicopterRepository get() {
    return newInstance();
  }

  public static FakeHelicopterRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FakeHelicopterRepository newInstance() {
    return new FakeHelicopterRepository();
  }

  private static final class InstanceHolder {
    private static final FakeHelicopterRepository_Factory INSTANCE = new FakeHelicopterRepository_Factory();
  }
}
