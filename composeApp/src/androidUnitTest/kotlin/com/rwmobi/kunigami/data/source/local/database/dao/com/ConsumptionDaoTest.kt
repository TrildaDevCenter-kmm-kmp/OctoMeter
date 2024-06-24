/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.data.source.local.database.dao.com

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.kunigami.data.source.local.database.OctometerDatabase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class ConsumptionDaoTest {

    private lateinit var database: OctometerDatabase

    @BeforeTest
    fun setupDatabase() {
        // We don't need Koin in this test
        stopKoin()

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder<OctometerDatabase>(
            context = context,
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertAndRetrieveConsumptionEntity() = runBlocking {
        val consumption = ConsumptionEntitySamples.sample1
        database.consumptionDao.insert(consumption)

        val retrieved = database.consumptionDao.getConsumptions(
            meterSerial = ConsumptionEntitySamples.sample1.meterSerial,
            intervalStart = ConsumptionEntitySamples.sample1.intervalStart - Duration.parse("1h"),
            intervalEnd = ConsumptionEntitySamples.sample1.intervalStart + Duration.parse("1h"),
        )

        assertEquals(1, retrieved.size)
        assertEquals(ConsumptionEntitySamples.sample1, retrieved[0])
    }

    @Test
    fun insertAndRetrieveMultipleConsumptionEntities() = runBlocking {
        val consumptions = listOf(
            ConsumptionEntitySamples.sample1,
            ConsumptionEntitySamples.sample2,
        )
        database.consumptionDao.insert(consumptions)

        val retrieved = database.consumptionDao.getConsumptions(
            meterSerial = ConsumptionEntitySamples.sample1.meterSerial,
            intervalStart = ConsumptionEntitySamples.sample1.intervalStart - Duration.parse("1h"),
            intervalEnd = ConsumptionEntitySamples.sample1.intervalStart + Duration.parse("1h"),
        )

        assertEquals(2, retrieved.size)
        assertEquals(consumptions[0], retrieved[0])
        assertEquals(consumptions[1], retrieved[1])
    }

    @Test
    fun getConsumptions_ShouldReturnEmptyList_IfMeterSerialHasNoDataInDb() = runBlocking {
        val consumption = ConsumptionEntitySamples.sample1
        database.consumptionDao.insert(consumption)

        val retrieved = database.consumptionDao.getConsumptions(
            meterSerial = "invalid-serial",
            intervalStart = ConsumptionEntitySamples.sample1.intervalStart,
            intervalEnd = ConsumptionEntitySamples.sample1.intervalEnd,
        )

        assertTrue(retrieved.isEmpty())
    }

    @Test
    fun getConsumptions_ShouldReturnEmptyList_IfMeterSerialHasDataButNotWithinRequestedRange() = runBlocking {
        val consumption = ConsumptionEntitySamples.sample1
        database.consumptionDao.insert(consumption)

        val retrieved = database.consumptionDao.getConsumptions(
            meterSerial = ConsumptionEntitySamples.sample1.meterSerial,
            intervalStart = ConsumptionEntitySamples.sample1.intervalEnd,
            intervalEnd = ConsumptionEntitySamples.sample1.intervalEnd + Duration.parse("1h"),
        )

        assertTrue(retrieved.isEmpty())
    }

    @Test
    fun getConsumptions_ShouldReturnOneEntry_IfIntervalStartAndIntervalEndCoverOneOfThem() = runBlocking {
        val consumptions = listOf(
            ConsumptionEntitySamples.sample1,
            ConsumptionEntitySamples.sample2,
        )
        database.consumptionDao.insert(consumptions)

        val retrieved = database.consumptionDao.getConsumptions(
            meterSerial = ConsumptionEntitySamples.sample1.meterSerial,
            intervalStart = ConsumptionEntitySamples.sample1.intervalStart,
            intervalEnd = ConsumptionEntitySamples.sample1.intervalEnd,
        )

        assertEquals(1, retrieved.size)
        assertEquals(consumptions[0], retrieved[0])
    }

    @Test
    fun clearConsumptionEntities() = runBlocking {
        val consumption = ConsumptionEntitySamples.sample1
        database.consumptionDao.insert(consumption)

        database.consumptionDao.clear()
        val retrieved = database.consumptionDao.getConsumptions(
            meterSerial = ConsumptionEntitySamples.sample1.meterSerial,
            intervalStart = ConsumptionEntitySamples.sample1.intervalStart - Duration.parse("1h"),
            intervalEnd = ConsumptionEntitySamples.sample1.intervalStart + Duration.parse("1h"),
        )

        assertTrue(retrieved.isEmpty())
    }
}