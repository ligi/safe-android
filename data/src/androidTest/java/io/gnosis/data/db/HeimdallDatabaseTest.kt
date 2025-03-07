package io.gnosis.data.db

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.gnosis.data.models.Safe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pm.gnosis.model.Solidity
import java.io.IOException
import java.math.BigInteger

// https://developer.android.com/training/data-storage/room/migrating-db-versions
@RunWith(AndroidJUnit4::class)
class HeimdallDatabaseTest {

    private val addressConverter = SolidityAddressConverter()

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        HeimdallDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        // Create earliest version of the database.
        helper.createDatabase(TEST_DB, 1).apply {
            close()
        }

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            HeimdallDatabase::class.java,
            TEST_DB
        ).build().apply {
            openHelper.writableDatabase
            close()
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        val safe = Safe(Solidity.Address(BigInteger.ONE), "safe_local_name")
        helper.createDatabase(TEST_DB, 1).apply {
            val rowId = insert(Safe.TABLE_NAME, OnConflictStrategy.REPLACE,
                ContentValues().apply {
                    put(Safe.COL_ADDRESS, addressConverter.toHexString(safe.address))
                    put(Safe.COL_LOCAL_NAME, safe.localName)
                })

            assert(rowId >= 0)

            close()
        }
    }

    companion object {
        private const val TEST_DB = "migration-test"
    }
}
