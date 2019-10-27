package jp.co.myowndict.view

import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.co.myowndict.R

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
