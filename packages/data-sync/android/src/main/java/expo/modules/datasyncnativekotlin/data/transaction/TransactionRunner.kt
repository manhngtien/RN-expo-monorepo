package expo.modules.datasyncnativekotlin.data.transaction

interface TransactionRunner {
    suspend fun <R> run(block: suspend () -> R): R
}
