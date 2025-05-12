package projectflower.vsort

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" != intent.type) {
                    return
                }

                val url = intent?.clipData?.getItemAt(0)?.text
                // Toast.makeText(this, url, Toast.LENGTH_LONG).show()

                url?.let {
                    val uri = it.toString().toUri()
                    val host = uri.host.toString()
                    var id: String? = null

                    if (host == "youtu.be") {
                        id = uri.pathSegments[0]
                    } else if (Regex("^(m\\.)*youtube.com").matches(host)) {
                        id = uri.getQueryParameter("v")
                    }

                    if (id != null) {
                        transportUrl(id)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // 再開した時に Intent が復元されるので、終了させる
        finishAndRemoveTask()
    }

    private fun transportUrl(id: String) {
        val launcherIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = "https://youtube.com/watch?v=${id}&list=UL01234567890".toUri()
        }

        startActivity(launcherIntent)
    }
}
