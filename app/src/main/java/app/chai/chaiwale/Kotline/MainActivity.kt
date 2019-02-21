package app.chai.chaiwale.Kotline

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import app.chai.chaiwale.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      /*  btnDownload.onClick {
            progressBar.progress = 0
            Fuel.download("http://httpbin.org/bytes/32768").destination { response, url ->
                File.createTempFile("boo", ".tmp")
            }.progress { readBytes, totalBytes ->
                val progress = readBytes.toFloat() / totalBytes.toFloat()
                progressBar.progress = progress.toInt()*100
            }.response { req, res, result ->
                Log.d("status result", result.component1().toString())
                Log.d("status res", res.responseMessage)
                Log.d("status req", req.url.toString())
            }
        }*/
    }
}


