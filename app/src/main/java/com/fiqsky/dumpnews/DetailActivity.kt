package com.fiqsky.dumpnews

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DetailActivity : AppCompatActivity() {

    private lateinit var articleWebView: WebView
    private lateinit var titleTextView: TextView
    private lateinit var timestampTextView: TextView
    private lateinit var collapsingToolbar: CollapsingToolbarLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Mengambil data dari intent
        val title = intent.getStringExtra("TITLE") ?: "Artikel"
        val content = intent.getStringExtra("CONTENT")
        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val timestamp = intent.getStringExtra("TIMESTAMP")

        // Inisialisasi toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Tombol kembali
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Inisialisasi collapsing toolbar
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        collapsingToolbar.title = title

        // Inisialisasi elemen UI lainnya
        val imageView: ImageView = findViewById(R.id.articleImage)
        titleTextView = findViewById(R.id.articleTitle)
        timestampTextView = findViewById(R.id.articleTimestamp)
        articleWebView = findViewById(R.id.articleWebView)

        // Menampilkan title
        titleTextView.text = title

        // Menampilkan timestamp
        timestampTextView.text = formatTimestamp(timestamp)

        // Memuat konten ke WebView
        setupWebView(content)

        // Memuat gambar artikel
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(imageView)
        }
    }

    // Fungsi untuk memuat konten ke WebView
    private fun setupWebView(content: String?) {
        articleWebView.settings.javaScriptEnabled = true
        articleWebView.settings.domStorageEnabled = true
        articleWebView.settings.loadWithOverviewMode = true
        articleWebView.settings.useWideViewPort = true

        if (!content.isNullOrEmpty()) {
            val responsiveContent = """
            <html>
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
                    <link href="https://cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
                    <style>
                        img {
                            max-width: 100%;
                            height: auto;
                        }
                        body {
                            font-size: 16px;
                            line-height: 1.6;
                            margin: 0;
                            padding: 0;
                        }
                        /* Tambahan class Quill.js */
                        /* Alignment */
                        .ql-align-center { text-align: center; }
                        .ql-align-right { text-align: right; }
                        .ql-align-justify { text-align: justify; }
                        .ql-align-left { text-align: left; }
                        /* Text Styles */
                        .ql-bold { font-weight: bold; }
                        .ql-italic { font-style: italic; }
                        .ql-underline { text-decoration: underline; }
                        .ql-strike { text-decoration: line-through; }
                        /* Header Styles */
                        .ql-size-small { font-size: 0.75em; }
                        .ql-size-large { font-size: 1.5em; }
                        .ql-size-huge { font-size: 2.5em; }
                        /* Blockquote */
                        blockquote {
                            border-left: 4px solid #ccc;
                            margin: 1.5em 10px;
                            padding: 0.5em 10px;
                            color: #666;
                            font-style: italic;
                        }
                        /* List Styles */
                        ul { list-style-type: disc; margin-left: 1.5em; }
                        ol { list-style-type: decimal; margin-left: 1.5em; }
                        .ql-indent-1 { margin-left: 2em; }
                        .ql-indent-2 { margin-left: 4em; }
                        .ql-indent-3 { margin-left: 6em; }
                        /* Links */
                        a { color: #1e88e5; text-decoration: none; }
                        a:hover { text-decoration: underline; }
                        /* Image */
                        img { max-width: 100%; height: auto; display: block; margin: 10px auto; }
                    </style>
                </head>
                <body>
                    $content
                </body>
            </html>
        """
            articleWebView.loadDataWithBaseURL(null, responsiveContent, "text/html", "UTF-8", null)
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
                date?.let { outputFormat.format(it) } ?: timestamp
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}