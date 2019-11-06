package jp.co.myowndict.view

import android.view.View
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.co.myowndict.R
import android.view.ViewGroup

fun Fragment.updateAppBar(
    title: String
) {
    val toolBar: Toolbar = view!!.findViewById(R.id.tool_bar)!!
    toolBar.also {
        it.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        it.navigationIcon = requireContext().getDrawable(R.drawable.ic_arrow_back_black_24dp)
        it.title = title
        it.setBackgroundColor(requireContext().getColor(R.color.white))
    }
}

fun Fragment.getStatusBarSize(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}

fun Fragment.getNavigationBarSize(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}

fun View.updateMargins(left: Int, right: Int, top: Int, bottom: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        requestLayout()
    }
}
