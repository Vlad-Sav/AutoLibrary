package kg.android.autolibrary.ui.base

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import kg.android.autolibrary.R

class DialogBuilder {
    /**
     * Builder of dialog for purchasing of app subscription
     *
     * @param context
     * @param positiveAction - action for positive button
     */
    fun buildDialog(context: Context, positiveAction: () -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.buy_dialog_title))
            builder.setMessage(context.getString(R.string.buy_dialog_description))
            val positiveButtonText = context.getString(R.string.buy_dialog_positive)
            builder.setPositiveButton(positiveButtonText) { dialog, which ->
                positiveAction.invoke()
            }
            builder.setNegativeButton(context.getString(R.string.buy_dialog_negative)) { dialog, which ->
            }
            builder.show()
    }
}