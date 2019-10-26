package jp.co.myowndict.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wada811.databinding.dataBinding

import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val binding by dataBinding<FragmentMainBinding>(R.layout.fragment_main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO: test
        binding.text.text = "Hello binding!!!"
    }
}
