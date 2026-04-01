package com.skyride.feature.booking;

import androidx.lifecycle.SavedStateHandle;
import com.skyride.core.data.repository.BookingRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class BookingViewModel_Factory implements Factory<BookingViewModel> {
  private final Provider<BookingRepository> bookingRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public BookingViewModel_Factory(Provider<BookingRepository> bookingRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.bookingRepositoryProvider = bookingRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public BookingViewModel get() {
    return newInstance(bookingRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static BookingViewModel_Factory create(
      Provider<BookingRepository> bookingRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new BookingViewModel_Factory(bookingRepositoryProvider, savedStateHandleProvider);
  }

  public static BookingViewModel newInstance(BookingRepository bookingRepository,
      SavedStateHandle savedStateHandle) {
    return new BookingViewModel(bookingRepository, savedStateHandle);
  }
}
