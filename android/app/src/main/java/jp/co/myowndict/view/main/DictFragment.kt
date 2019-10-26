package jp.co.myowndict.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentDictBinding

class DictFragment : DaggerFragment() {

    private val binding by dataBinding<FragmentDictBinding>(R.layout.fragment_dict)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}