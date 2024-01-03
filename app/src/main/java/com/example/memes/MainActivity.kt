package com.example.memes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    private var loadedMemeUrls = ArrayList<String>() // to save the already loaded meme urls



    private var CurrentMemeUrl : String = "" // to store url of current meme
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        Toast.makeText(this,"If the meme doesn't load, try pressing Next Button!",Toast.LENGTH_LONG).show()

        load_memes() // loading the first meme


        val buttonShare : Button = findViewById(R.id.share_button)

        buttonShare.setOnClickListener { // implementing share button

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT , "Hey, Checkout these Meme I found on Reddit $CurrentMemeUrl")
            val chooser = Intent.createChooser(intent,"Share this Meme using..")

            startActivity(chooser)

        }


        val buttonNext : Button = findViewById(R.id.next_button)

        buttonNext.setOnClickListener {
                load_memes() // loading next meme
        }

    }


    private fun load_memes()
    {

        val progressBar: ProgressBar = findViewById(R.id.progressBar2)


        val img : ImageView = findViewById(R.id.meme_image)
        img.visibility = View.GONE


        val url = "https://meme-api.com/gimme"
        // Request a string response from the provided URL.

        progressBar.visibility = View.VISIBLE
        val JSON_request =JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                CurrentMemeUrl = response.getString("url")

                if (!loadedMemeUrls.contains( CurrentMemeUrl )) {

                    Glide.with(this).load( CurrentMemeUrl ).into(img)
                    progressBar.visibility = View.GONE
                    img.visibility = View.VISIBLE
                    loadedMemeUrls.add(CurrentMemeUrl)
                }
                else
                {
                    load_memes()
                }

            },
            {
                //nothing call the function again
                load_memes()
            })


        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(JSON_request)
    }


}