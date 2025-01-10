package com.fiqsky.dumpnews

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var articlesAdapter: ArticlesAdapter
    private val database = FirebaseDatabase.getInstance().reference
    private val articles = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        // Inisialisasi RecyclerView dan Adapter
        recyclerView = findViewById(R.id.recyclerViewArticles)
        articlesAdapter = ArticlesAdapter(this, articles) { article ->
            // Arahkan ke DetailActivity ketika item di-click
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("TITLE", article.title)
                putExtra("CONTENT", article.content)
                putExtra("IMAGE_URL", article.imageUrl)
                putExtra("TIMESTAMP", article.timestamp)
            }
            startActivity(intent)
        }

        // Set LayoutManager untuk RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = articlesAdapter

        // Fetch data artikel dari Firebase
        fetchArticles()
    }

    private fun fetchArticles() {
        // Ambil data artikel dari Firebase Realtime Database
        database.child("articles").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                articles.clear()
                for (articleSnapshot in snapshot.children) {
                    val article = articleSnapshot.getValue(Article::class.java)
                    article?.let {
                        articles.add(it)
                    }
                }
                // Notifikasi perubahan data ke Adapter
                articlesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Menangani error ketika pengambilan data gagal
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
