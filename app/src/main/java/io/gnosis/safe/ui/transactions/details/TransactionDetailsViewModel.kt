package io.gnosis.safe.ui.transactions.details

import io.gnosis.data.models.TransactionDetails
import io.gnosis.data.repositories.SafeRepository
import io.gnosis.data.repositories.TransactionRepository
import io.gnosis.safe.ui.base.AppDispatchers
import io.gnosis.safe.ui.base.BaseStateViewModel
import javax.inject.Inject

class TransactionDetailsViewModel
@Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val safeRepository: SafeRepository,
    appDispatchers: AppDispatchers
) : BaseStateViewModel<TransactionDetailsViewState>(appDispatchers) {

    override fun initialState() = TransactionDetailsViewState(null, ViewAction.Loading(true))

    fun loadDetails(txId: String) {
        safeLaunch {
            val txDetails = transactionRepository.getTransactionDetails(txId)
            updateState { TransactionDetailsViewState(txDetails, ViewAction.Loading(false)) }
        }
    }
}

data class TransactionDetailsViewState(
    val txDetails: TransactionDetails?,
    override var viewAction: BaseStateViewModel.ViewAction?
) : BaseStateViewModel.State
