package io.gnosis.safe.ui.settings.view

import android.content.Context
import android.content.res.TypedArray
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.gnosis.safe.R
import io.gnosis.safe.databinding.ViewAddressItemBinding
import pm.gnosis.crypto.utils.asEthereumAddressChecksumString
import pm.gnosis.model.Solidity
import pm.gnosis.svalinn.common.utils.*
import timber.log.Timber

class AddressItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding by lazy { ViewAddressItemBinding.inflate(LayoutInflater.from(context), this) }

    init {
        readAttributesAndSetupFields(context, attrs)
    }

    var address: Solidity.Address? = null
        set(value) {
            with(binding) {
                blockies.setAddress(value)
                address.text = value?.formatOwnerAddress()
                binding.link.setOnClickListener {
                    context.openUrl(
                        context.getString(
                            R.string.etherscan_address_url,
                            value?.asEthereumAddressChecksumString()
                        )
                    )
                }
                binding.address.setOnClickListener {
                    context.copyToClipboard(context.getString(R.string.address_copied), address.text.toString()) {
                        snackbar(view = root, textId = R.string.copied_success)
                    }
                }
            }
            field = value
        }

    var showSeparator: Boolean = false
        set(value) {
            binding.addressDivider.visible(value)
            field = value
        }

    private fun Solidity.Address.formatOwnerAddress(prefixLength: Int = 6, suffixLength: Int = 4): Spannable =
        SpannableStringBuilder(this.asEthereumAddressChecksumString()).apply {
            setSpan(
                ForegroundColorSpan(context.getColorCompat(R.color.gnosis_dark_blue)),
                0,
                prefixLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(context.getColorCompat(R.color.gnosis_dark_blue)),
                length - suffixLength,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

    private fun readAttributesAndSetupFields(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AddressItem,
            0, 0
        ).also {
            runCatching {
                applyAttributes(context, it)
            }
                .onFailure { Timber.e(it) }
            it.recycle()
        }
    }

    private fun applyAttributes(context: Context, a: TypedArray) {
        binding.addressDivider.visible(a.getBoolean(R.styleable.AddressItem_show_separator, false))
    }
}
