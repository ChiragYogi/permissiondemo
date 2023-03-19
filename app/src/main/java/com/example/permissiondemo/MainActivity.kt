package com.example.permissiondemo

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.permissiondemo.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var latestTmpUri: Uri? = null

    private var isAnimationIsOnGoing = false

    val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSucess ->
            if (isSucess) {
                latestTmpUri?.let {
                    Glide.with(this).load(it).into(binding.imageView)

                }
            }

        }

    val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { data ->

            data?.let {
                latestTmpUri = it
                Glide.with(this).load(latestTmpUri).into(binding.imageView)
            }
        }


    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonClick.setOnClickListener {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                cameraLauncher.launch(latestTmpUri)
            }
        }

        binding.buttonClickGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.buttonRotateImage.setOnClickListener {

            val raoteAminateino = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)

            isAnimationIsOnGoing = true


            binding.imageView.startAnimation(raoteAminateino)
        }
    }


    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", this.cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            this.applicationContext,
            "com.example.permissiondemo.provider",
            tmpFile
        )
    }
}
