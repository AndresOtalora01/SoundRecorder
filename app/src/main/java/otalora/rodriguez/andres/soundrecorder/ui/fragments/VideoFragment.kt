package otalora.rodriguez.andres.soundrecorder.ui.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import otalora.rodriguez.andres.soundrecorder.R
import otalora.rodriguez.andres.soundrecorder.databinding.FragmentHomeBinding
import otalora.rodriguez.andres.soundrecorder.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private lateinit var videoView : VideoView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoView = binding.videoView
        videoView.setVideoURI(Uri.parse("android.resource://" + (context?.packageName + "/" + R.raw.grz)))
        val mediaController = MediaController(context)
        videoView.setMediaController(mediaController)
        videoView.start()

    }

}