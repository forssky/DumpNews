package com.fiqsky.dumpnews

import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DetailActivity : AppCompatActivity() {

    private lateinit var articleWebView: WebView
    private lateinit var timestampTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Mengambil data dari intent
        val title = intent.getStringExtra("TITLE")
        val content = intent.getStringExtra("CONTENT")
        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val timestamp = intent.getStringExtra("TIMESTAMP") // Ambil timestamp

        // Menghubungkan UI dengan data
        val titleTextView: TextView = findViewById(R.id.articleTitle)
        articleWebView = findViewById(R.id.articleWebView)
        timestampTextView = findViewById(R.id.articleTimestamp)

        // Menampilkan title
        titleTextView.text = title

        // Menampilkan timestamp
        timestampTextView.text = timestamp ?: "Tanggal tidak tersedia"

        // Mengkonfigurasi WebView
        articleWebView.settings.javaScriptEnabled = true
        articleWebView.settings.domStorageEnabled = true
        articleWebView.settings.loadWithOverviewMode = true
        articleWebView.settings.useWideViewPort = true

        // Memuat konten HTML ke WebView dengan penyesuaian
        if (!content.isNullOrEmpty()) {
            val responsiveContent = """
                <html>
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
                        <style>
                            img {
                                max-width: 100%;
                                height: auto;
                            }
                            body {
                                font-size: 16px;
                                line-height: 1.6;
                            }
                        </style>
                    </head>
                    <body>
                        $content
                    </body>
                </html>
            """
            articleWebView.loadDataWithBaseURL(null, responsiveContent, "text/html", "UTF-8", null)
        }

        // Memuat gambar artikel jika ada
        if (!imageUrl.isNullOrEmpty()) {
            val imageView: ImageView = findViewById(R.id.articleImage)
            Picasso.get().load(imageUrl).into(imageView)
        }
    }

    // Fungsi untuk memformat timestamp
    private fun formatTimestamp(timestamp: String?): String {
        return try {
            if (timestamp != null && timestamp.isNotEmpty()) {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", Locale.getDefault())
                val date = inputFormat.parse(timestamp)
                if (date != null) {
                    outputFormat.format(date)
                } else {
                    timestamp
                }
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}