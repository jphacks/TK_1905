package jp.co.myowndict.view.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentRecordingBinding

class RecordingFragment : DaggerFragment() {

    private val binding by dataBinding<FragmentRecordingBinding>(R.layout.fragment_recording)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    fun startTagAnimation() {
        if (!isAdded) return
        ObjectAnimator.ofFloat(binding.tag, "x", -binding.tag.width.toFloat(), 0f).apply {
            duration = 300
            start()
        }
    }

    fun hideTag() {
        if (!isAdded) return
        binding.tag.x = -binding.tag.width.toFloat()
    }
}
