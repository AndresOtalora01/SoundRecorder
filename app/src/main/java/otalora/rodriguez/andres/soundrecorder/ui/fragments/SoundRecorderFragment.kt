package otalora.rodriguez.andres.soundrecorder.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.MessageQueue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import otalora.rodriguez.andres.soundrecorder.R
import otalora.rodriguez.andres.soundrecorder.databinding.FragmentHomeBinding
import otalora.rodriguez.andres.soundrecorder.databinding.FragmentSoundRecorderBinding
import java.io.IOException

class SoundRecorderFragment : Fragment() {
    private var _binding: FragmentSoundRecorderBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSoundRecorderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonRecord.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(requireActivity(), permissions,0)
            } else {
                startRecording()
            }
        }

        binding.buttonPause.setOnClickListener {
            pauseRecording()
        }

        binding.buttonStop.setOnClickListener {
            stopRecording()
        }

        binding.buttonPlay.setOnClickListener {
            playRecording()
        }
    }

    private fun startRecording() {
        try {
            output = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(output)
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            binding.txtPauseRecording.text = "Grabación comenzada!"
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun stopRecording(){
        if(state){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state = false
            recordingStopped = false
            binding.txtPauseRecording.text = "Grabación detenida!"
        }else{
            Toast.makeText(context, "No se está grabando!", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        if(state) {
            if(!recordingStopped){
                binding.txtPauseRecording.text = "Grabación pausada!"
                mediaRecorder?.pause()
                recordingStopped = true
            }else{
                resumeRecording()
            }
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        Toast.makeText(context,"Continua!", Toast.LENGTH_SHORT).show()
        mediaRecorder?.resume()
        binding.txtPauseRecording.text = "Continua la grabación!"
        recordingStopped = false
    }

    private fun playRecording() {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(output)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }
}