package jp.co.myowndict.extensions

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("android:htmlText")
fun TextView.setHtmlText(text: String?) {
    text ?: return
    setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT))
}
