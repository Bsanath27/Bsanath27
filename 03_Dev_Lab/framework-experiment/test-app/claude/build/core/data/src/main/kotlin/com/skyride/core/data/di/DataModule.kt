package com.skyrik.core.data.di

import com.skyrik.core.data.fake.FakeBookingRepository
import com.skyrik.core.data.fake.FakeHelicopterRepository
import com.skyrik.core.data.repository.BookingRepository
import com.skyrik.core.data.repository.HelicopterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module binding fake repository implementations to their interfaces.
 *
 * To switch to real API implementations:
 *  1. Create a `RealBookingRepository` that calls your REST/gRPC endpoints.
 *  2. Replace [FakeBookingRepository] with the real class in [bindBookingRepository].
 *  3. (Optional) Use build-variant–specific modules or @Named qualifiers.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        fake: FakeBookingRepository,
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindHelicopterRepository(
        fake: FakeHelicopterRepository,
    ): HelicopterRepository
}
