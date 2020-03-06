package fr.isen.berton.projectapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

/*    private lateinit var fusedLocationClient: FusedLocationProviderClient
    public var latitude : Double? = 0.0
    public var longitude: Double? = 0.0
    private var mLatitudeTextView: TextView? = null
    private var mLongitudeTextView: TextView? = null*/

    companion object {
        val permission_request_code = 3
        val gpsPermissionRequestCode = 2
    }

    lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Test()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        requestPermission(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            gpsPermissionRequestCode
        ) {
            startGPS()

        }




    }
fun Test(){
    val database = FirebaseDatabase.getInstance()
    val data = database.getReference("Users")
    val newId = data.push().key.toString()
    val User = Users(newId,"louis le patron")
    data.child(newId).setValue(User)
}
    fun requestPermission(permissionToRequest: String, requestCode: Int, handler: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this,
                permissionToRequest
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionToRequest)) {
                //display toast
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(permissionToRequest), requestCode)
            }
        } else {
            handler()
        }
    }

    @SuppressLint("MissingPermission")
    fun startGPS() {
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null)
        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        location?.let {
            refreshPositionuser(it)
        }
    }

    fun refreshPositionuser(location: Location) {
        latitudeedittext.text = "Latitude: ${location.latitude}"
        longitudeedittext.text = "Longitude: ${location.longitude}"
    }

    fun onLocationChanged(location: Location?) {
        location?.let {
            refreshPositionuser(it)
        }
    }

    private fun LocationManager.requestSingleUpdate(
        networkProvider: String,
        permissionsActivity: MainActivity,
        nothing: Nothing?
    ) {

    }

    fun reload(view: View){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    @SuppressLint("MissingPermission")
    public fun insideIsen(view: View) {

        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (location.longitude < 5.98 && location.longitude > 5.9 && location.latitude < 43.2 && location.latitude > 43.05) {



            val builder = AlertDialog.Builder(this)
            builder.setCancelable(true)

            builder.setMessage("Vous etes dans l'Isen !!")

            val dialog = builder.create()
            dialog.show()

        } else {

            val builder = AlertDialog.Builder(this)
            builder.setCancelable(true)

            builder.setMessage("Vous n'etes pas dans l'Isen !!")

            val dialog = builder.create()
            dialog.show()

        }
    }
}

