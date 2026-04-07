package expo.modules.datasyncnativekotlin.data.transaction

import androidx.room.withTransaction
import expo.modules.datasyncnativekotlin.data.local.database.AppDatabase

class RoomTransactionRunner(
    private val database: AppDatabase
) : TransactionRunner {
    override suspend fun <R> run(block: suspend () -> R): R =
        database.withTransaction(block)
}
