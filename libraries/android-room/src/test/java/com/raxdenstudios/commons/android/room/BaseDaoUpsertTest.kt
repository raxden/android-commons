package com.raxdenstudios.commons.android.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BaseDaoUpsertTest {

    private lateinit var database: TestDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.getApplication(), TestDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Given an existing parent with children, When upserted, Then children are kept and columns updated`() =
        runTest {
            database.parentDao().insert(ParentEntity(id = 1, name = "old"))
            database.childDao().insert(ChildEntity(id = 10, parentId = 1))

            database.parentDao().upsert(ParentEntity(id = 1, name = "new"))

            assertThat(database.parentDao().all()).containsExactly(ParentEntity(id = 1, name = "new"))
            assertThat(database.childDao().all()).containsExactly(ChildEntity(id = 10, parentId = 1))
        }

    @Test
    fun `Given a new row, When it is upserted, Then it is inserted`() = runTest {
        database.parentDao().upsert(ParentEntity(id = 2, name = "created"))

        assertThat(database.parentDao().all()).containsExactly(ParentEntity(id = 2, name = "created"))
    }

    @Test
    fun `Given existing and new rows, When the list is upserted, Then existing are updated and new inserted`() =
        runTest {
            database.parentDao().insert(ParentEntity(id = 1, name = "old"))

            database.parentDao().upsert(listOf(ParentEntity(id = 1, name = "new"), ParentEntity(id = 2, name = "two")))

            assertThat(database.parentDao().all())
                .containsExactly(ParentEntity(id = 1, name = "new"), ParentEntity(id = 2, name = "two"))
        }

    @Entity(tableName = "parent")
    data class ParentEntity(
        @PrimaryKey val id: Long,
        val name: String,
    )

    @Entity(
        tableName = "child",
        foreignKeys = [
            ForeignKey(
                entity = ParentEntity::class,
                parentColumns = ["id"],
                childColumns = ["parentId"],
                onDelete = ForeignKey.CASCADE,
            ),
        ],
    )
    data class ChildEntity(
        @PrimaryKey val id: Long,
        val parentId: Long,
    )

    @Dao
    interface ParentDao : BaseDao<ParentEntity> {

        @Query("SELECT * FROM parent ORDER BY id")
        suspend fun all(): List<ParentEntity>
    }

    @Dao
    interface ChildDao : BaseDao<ChildEntity> {

        @Query("SELECT * FROM child ORDER BY id")
        suspend fun all(): List<ChildEntity>
    }

    @Database(entities = [ParentEntity::class, ChildEntity::class], version = 1, exportSchema = false)
    abstract class TestDatabase : RoomDatabase() {
        abstract fun parentDao(): ParentDao
        abstract fun childDao(): ChildDao
    }
}
