package course.labs.alarmslab

import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.util.Base64
import android.util.Log
import android.widget.Toast

import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.Key
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.net.ssl.HttpsURLConnection

class AlarmTweetService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (intent.extras != null) {

            mRandom = SecureRandom()

            NetworkingTask().execute(intent.extras.getString(AlarmCreateActivity.TWEET_STRING))

        }

        return Service.START_STICKY
    }

    inner class NetworkingTask : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg args: String): Boolean? {
            try {
                return postTweet(args[0])
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(TAG, "COULDN'T GET IT")
            }

            return false
        }

        override fun onPostExecute(result: Boolean?) {
            Toast.makeText(applicationContext,
                    "The Tweet was submitted with result:" + result!!,
                    Toast.LENGTH_LONG).show()
        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        // The tweets would be visible at the twitter handle @UMDAndroid
        // Notification ID to allow for future updates
        private val TAG = "AlarmTweetService"


        // Consumer Key
        private val CONSUMER_KEY = "AwK1CjvJ9DMJlQf5bGLEW5uj0"

        // Consumer Secret
        private val CONSUMER_SECRET = "Vbynk2ka6wghFvVaWCLLl1vTpabNlehoPbdueXkPMPO7vr4Ojp"

        // Access Token
        private val ACCESS_TOKEN = "1187089419305672705-D5aYNVyKJHJmhOWrd19HHPjl14FVDh"

        // Access Token Secret
        private val ACCESS_TOKEN_SECRET = "0zEQJ9ekgJZ8tNDuTVKXFRs5p1BwTj1aSSoLKjCmZSzVn"

        // networking variables
        private var mRandom: SecureRandom? = null

        @Throws(IOException::class)
        private fun postTweet(status: String): Boolean {
            var connection: HttpsURLConnection? = null
            val nonce = newNonce()
            val timestamp = timestamp()

            try {

                // URL for updating the Twitter status
                val url = URL(
                        "https://api.twitter.com/1.1/statuses/update.json")
                
                connection = url.openConnection() as HttpsURLConnection

                connection.doOutput = true
                connection.doInput = true

                connection.requestMethod = "POST"

                connection.setRequestProperty("Host", "api.twitter.com")
                connection.setRequestProperty("User-Agent", "TwitterNetworkingLab")
                connection.setRequestProperty("Authorization", "OAuth "
                        + "oauth_consumer_key=\"" + CONSUMER_KEY + "\", "
                        + "oauth_nonce=\"" + nonce + "\", " + "oauth_signature=\""
                        + signature(status, nonce, timestamp) + "\", "
                        + "oauth_signature_method=\"HMAC-SHA1\", "
                        + "oauth_timestamp=\"" + timestamp + "\", "
                        + "oauth_token=\"" + ACCESS_TOKEN + "\", "
                        + "oauth_version=\"1.0\"")
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded")

                Log.d(TAG, connection.requestProperties.toString())

                // This POST request needs a body
                // This is how you write to the body

                val out = OutputStreamWriter(
                        connection.outputStream)

                // Twitter status is added here.

                out.write("status=" + URLEncoder.encode(status, "UTF-8"))

                out.flush()
                out.close()

                val test = "status=" + URLEncoder.encode(status, "UTF-8")
                Log.i(TAG, test)
                val response = JSONObject(read(connection))

                if (response != null) {
                    Log.d(TAG, "The tweet seems to have been posted successfully!")
                }

                return response != null

            } catch (e: MalformedURLException) {
                throw IOException("Invalid URL.", e)
            } catch (e: JSONException) {
                throw IOException("Invalid response from Twitter", e)
            } finally {
                connection?.disconnect()
            }
        }

        private fun read(connection: HttpsURLConnection): String {
            val stringBuffer = StringBuffer()
            try {
                val reader = BufferedReader(InputStreamReader(
                        connection.inputStream))
                reader.forEachLine { stringBuffer.append(it+"\n") }

            } catch (e: IOException) {
                try {
                    Log.i(TAG,
                            "Error reponse from twitter: " + connection.responseMessage)
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

                stringBuffer.append("Failed")
            }

            return stringBuffer.toString()
        }

        private fun newNonce(): String {
            val random = ByteArray(32)
            mRandom!!.nextBytes(random)

            var nonce = Base64.encodeToString(random, Base64.NO_WRAP)
            nonce = nonce.replace("[^a-zA-Z0-9\\s]".toRegex(), "")
            return nonce

        }

        private fun timestamp(): String {
            val time = System.currentTimeMillis() / 1000L
            return time.toString()
        }

        private fun signature(status: String, nonce: String,
                              timestamp: String): String {
            var status = status
            var nonce = nonce
            var timestamp = timestamp
            var output = ""
            // Obviously you can use a TreeMap with concurrency to implement this,
            // but this way you can see what is going on
            try {
                // Gather all parameters used for the status post
                var signatureBaseString = ""
                var parameterString = ""
                var signingKey = ""
                var signature = ""

                val httpMethod = "POST"
                var baseURL = "https://api.twitter.com/1.1/statuses/update.json"

                var consumerKey = CONSUMER_KEY
                var encMethod = "HMAC-SHA1"
                var token = ACCESS_TOKEN
                var version = "1.0"

                var kStatus = "status"
                var kKey = "oauth_consumer_key"
                var kNonce = "oauth_nonce"
                var kSigMeth = "oauth_signature_method"
                var kTimestamp = "oauth_timestamp"
                var kToken = "oauth_token"
                var kVersion = "oauth_version"

                // encode the parameters put into the http request header separately
                consumerKey = URLEncoder.encode(consumerKey, "UTF-8")
                nonce = URLEncoder.encode(nonce, "UTF-8")
                encMethod = URLEncoder.encode(encMethod, "UTF-8")
                timestamp = URLEncoder.encode(timestamp, "UTF-8")
                token = URLEncoder.encode(token, "UTF-8")
                version = URLEncoder.encode(version, "UTF-8")
                status = URLEncoder.encode(status, "UTF-8")
                status = status.replace("+", "%20") // URLEncoder encodes " " to
                // "+", which is incorrect
                // for twitter encoding

                kKey = URLEncoder.encode(kKey, "UTF-8")
                kNonce = URLEncoder.encode(kNonce, "UTF-8")
                kSigMeth = URLEncoder.encode(kSigMeth, "UTF-8")
                kTimestamp = URLEncoder.encode(kTimestamp, "UTF-8")
                kToken = URLEncoder.encode(kToken, "UTF-8")
                kVersion = URLEncoder.encode(kVersion, "UTF-8")
                kStatus = URLEncoder.encode(kStatus, "UTF-8")

                // append the parameters together with = and &
                parameterString = (kKey + "=" + consumerKey + "&" + kNonce + "="
                        + nonce + "&" + kSigMeth + "=" + encMethod + "&"
                        + kTimestamp + "=" + timestamp + "&" + kToken + "=" + token
                        + "&" + kVersion + "=" + version + "&" + kStatus + "="
                        + status)

                // build the signature base string
                signatureBaseString += httpMethod
                signatureBaseString += "&"
                baseURL = URLEncoder.encode(baseURL, "UTF-8")
                signatureBaseString += baseURL
                signatureBaseString += "&"
                parameterString = URLEncoder.encode(parameterString, "UTF-8")
                signatureBaseString += parameterString
                Log.d(TAG, signatureBaseString)

                // generate the HMAC-SHA1 signing key with the application and
                // account secrets
                signingKey = (URLEncoder.encode(CONSUMER_SECRET, "UTF-8") + "&"
                        + URLEncoder.encode(ACCESS_TOKEN_SECRET, "UTF-8"))

                // this should be self explanatory
                val bytes = signingKey.toByteArray()
                val key = SecretKeySpec(bytes, 0, bytes.size, "HmacSHA1")
                val mac = Mac.getInstance("HmacSHA1")
                mac.init(key)
                val signatureBytes = mac.doFinal(signatureBaseString.toByteArray())
                signature = Base64.encodeToString(signatureBytes, Base64.NO_WRAP)
                signature = URLEncoder.encode(signature, "UTF-8")

                output = signature

            } catch (e: UnsupportedEncodingException) {
                Log.d(TAG, "Error encoding parameters")
                e.printStackTrace()
            } catch (e: InvalidKeyException) {
                Log.d(TAG, "Error encrypting signature")
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                Log.d(TAG, "Invalid algorithm for signature")
                e.printStackTrace()
            }

            return output
        }
    }

}