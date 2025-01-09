package com.fiqsky.dumpnews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ArticlesAdapter(
    private val context: Context,
    private val articles: List<Article>,
    private val onClick: (Article) -> Unit
) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int = articles.size

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.articleTitle)
        private val timestampTextView: TextView = itemView.findViewById(R.id.articleTimestamp)
        private val articleImageView: ImageView = itemView.findViewById(R.id.articleImage)

        fun bind(article: Article) {
            titleTextView.text = article.title

            // Mengubah format timestamp
            val formattedTimestamp = formatTimestamp(article.timestamp)
            timestampTextView.text = formattedTimestamp

            // Memuat gambar artikel
            Picasso.get().load(article.imageUrl).into(articleImageView)

            // Klik item
            itemView.setOnClickListener {
                onClick(article)
            }
        }

        // Fungsi untuk memformat timestamp
        private fun formatTimestamp(timestamp: String): String {
            return try {
                // Parsing timestamp ISO 8601 (misalnya "2025-01-09T11:58:27.081Z")
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Mengatur zona waktu ke UTC

                // Format output yang diinginkan: "Hari, Tanggal Bulan Tahun"
                val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

                val date = inputFormat.parse(timestamp)
                if (date != null) {
                    outputFormat.format(date)
                } else {
                    timestamp // Jika format tidak valid, kembalikan original timestamp
                }
            } catch (e: Exception) {
                timestamp // Jika ada error dalam parsing, kembalikan original timestamp
            }
        }
    }
}
