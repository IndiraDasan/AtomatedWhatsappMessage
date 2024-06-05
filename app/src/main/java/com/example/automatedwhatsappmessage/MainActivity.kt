package com.example.automatedwhatsappmessage

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.broadcastreceiver.CallDetectingService
import com.example.automatedwhatsappmessage.databinding.ActivityMainBinding
import com.example.automatedwhatsappmessage.networkvalidation.Networkconnection
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    lateinit var  actionBarDrawerToggle: ActionBarDrawerToggle

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var dialog: AlertDialog? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        val networkConnection = Networkconnection(applicationContext)
        networkConnection.observe(this) { isconnected ->
            if (isconnected){
                val internetConnection = Snackbar.make(binding.root,"Connected to Internet", Snackbar.LENGTH_LONG)
                internetConnection.show()
                internetConnection.view.setBackgroundColor(Color.parseColor("#008000"))
            } else{
                val snack = Snackbar.make(binding.root,"No Internet", Snackbar.LENGTH_LONG)
                snack.view.setBackgroundColor(Color.parseColor("#FF0000"))
                snack.show()
            }
        }

        AppPreferences.init(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        checkAppPermissions()

        startService(Intent(applicationContext, CallDetectingService::class.java))


        // init drawer layout
        // init action bar drawer toggle
        actionBarDrawerToggle = ActionBarDrawerToggle(this,binding.drwerLayout,R.string.nav_open,R.string.nav_close)
        // add a drawer listener into  drawer layout
        binding.drwerLayout.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.syncState()

        // show menu icon and back icon while drawer open

       // binding.navigationView.visibility = View.GONE
        navClick()

        if(!checkAccessibilityPermission()){
         //   Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun disableTheSwipeGesture(){
        binding.drwerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }
    fun enableTheSwipeGesture() {
        binding.drwerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // check conndition for drawer item with menu item
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            true
        }else{
            super.onOptionsItemSelected(item)
        }

    }

    private fun navClick() {
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.home ->{

                    val navController = findNavController(R.id.nav_host_fragment)
                    if (navController.currentDestination?.id==R.id.profileFragment){
                        navController.navigate(R.id.action_profileFragment_to_homeFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.contactFragment){
                        navController.navigate(R.id.action_contactFragment_to_homeFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.permissionFragment){
                        navController.navigate(R.id.action_permissionFragment_to_homeFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.configurationFragment){
                        navController.navigate(R.id.action_configurationFragment_to_homeFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.aboutFragment){
                        navController.navigate(R.id.action_aboutFragment_to_homeFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.templateFragment){
                        navController.navigate(R.id.action_templateFragment_to_homeFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.subscriptionFragment){
                        navController.navigate(R.id.action_subscriptionFragment_to_homeFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.viewFragment){
                        navController.navigate(R.id.action_viewFragment_to_homeFragment)

                    }

                }
                R.id.profile -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    if (navController.currentDestination?.id==R.id.homeFragment){
                        navController.navigate(R.id.action_homeFragment_to_profileFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.contactFragment){
                        navController.navigate(R.id.action_contactFragment_to_profileFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.permissionFragment){
                        navController.navigate(R.id.action_permissionFragment_to_profileFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.configurationFragment){
                        navController.navigate(R.id.action_configurationFragment_to_profileFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.aboutFragment){
                        navController.navigate(R.id.action_aboutFragment_to_profileFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.templateFragment){
                        navController.navigate(R.id.action_templateFragment_to_profileFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.subscriptionFragment){
                        navController.navigate(R.id.action_subscriptionFragment_to_profileFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.viewFragment){
                        navController.navigate(R.id.action_viewFragment_to_profileFragment)

                    }
                }
                R.id.contact -> {
                    val navController = findNavController(R.id.nav_host_fragment)

                    if (navController.currentDestination?.id==R.id.homeFragment) {
                        navController.navigate(R.id.action_homeFragment_to_contactFragment)

                    }else if (navController.currentDestination?.id==R.id.profileFragment){
                        navController.navigate(R.id.action_profileFragment_to_contactFragment)

                    }  else if (navController.currentDestination?.id==R.id.subscriptionFragment){
                        navController.navigate(R.id.action_subscriptionFragment_to_contactFragment)

                    } else if (navController.currentDestination?.id==R.id.permissionFragment){
                        navController.navigate(R.id.action_permissionFragment_to_contactFragment)

                    } else if (navController.currentDestination?.id==R.id.configurationFragment){
                        navController.navigate(R.id.action_configurationFragment_to_contactFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.aboutFragment){
                        navController.navigate(R.id.action_aboutFragment_to_contactFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.templateFragment){
                        navController.navigate(R.id.action_templateFragment_to_contactFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.viewFragment){
                        navController.navigate(R.id.action_viewFragment_to_contactFragment)

                    }

                }
                R.id.about -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    if (navController.currentDestination?.id==R.id.homeFragment) {
                        navController.navigate(R.id.action_homeFragment_to_aboutFragment)

                    }else if (navController.currentDestination?.id==R.id.profileFragment){
                        navController.navigate(R.id.action_profileFragment_to_aboutFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.configurationFragment){
                        navController.navigate(R.id.action_configurationFragment_to_aboutFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.templateFragment){
                        navController.navigate(R.id.action_templateFragment_to_aboutFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.subscriptionFragment){
                        navController.navigate(R.id.action_subscriptionFragment_to_aboutFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.permissionFragment){
                        navController.navigate(R.id.action_permissionFragment_to_aboutFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.contactFragment){
                        navController.navigate(R.id.action_contactFragment_to_aboutFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.viewFragment){
                        navController.navigate(R.id.action_viewFragment_to_aboutFragment)

                    }


                }
                R.id.tempalte -> {

                    val navController = findNavController(R.id.nav_host_fragment)

                    if (navController.currentDestination?.id==R.id.homeFragment){
                        navController.navigate(R.id.action_homeFragment_to_templateFragment)

                    }else if (navController.currentDestination?.id==R.id.profileFragment){
                        navController.navigate(R.id.action_profileFragment_to_templateFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.configurationFragment){
                        navController.navigate(R.id.action_configurationFragment_to_templateFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.permissionFragment){
                        navController.navigate(R.id.action_permissionFragment_to_templateFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.subscriptionFragment){
                        navController.navigate(R.id.action_subscriptionFragment_to_templateFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.contactFragment){
                        navController.navigate(R.id.action_contactFragment_to_templateFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.aboutFragment){
                        navController.navigate(R.id.action_aboutFragment_to_templateFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.viewFragment){
                        navController.navigate(R.id.action_viewFragment_to_templateFragment)
                    }


                }
                R.id.configuration -> {
                    val navController = findNavController(R.id.nav_host_fragment)

                    if (navController.currentDestination?.id==R.id.homeFragment) {
                        navController.navigate(R.id.action_homeFragment_to_configurationFragment)

                    }else if (navController.currentDestination?.id==R.id.profileFragment){
                        navController.navigate(R.id.action_profileFragment_to_configurationFragment)
                    }

                    else if (navController.currentDestination?.id==R.id.aboutFragment){
                        navController.navigate(R.id.action_aboutFragment_to_configurationFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.templateFragment){
                        navController.navigate(R.id.action_templateFragment_to_configurationFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.permissionFragment){
                        navController.navigate(R.id.action_permissionFragment_to_configurationFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.subscriptionFragment){
                        navController.navigate(R.id.action_subscriptionFragment_to_configurationFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.contactFragment){
                        navController.navigate(R.id.action_contactFragment_to_configurationFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.viewFragment){
                        navController.navigate(R.id.action_viewFragment_to_configurationFragment)

                    }

                }

                R.id.subscription -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    if (navController.currentDestination?.id==R.id.homeFragment) {
                        navController.navigate(R.id.action_homeFragment_to_subscriptionFragment2)

                    }else if (navController.currentDestination?.id==R.id.profileFragment){
                        navController.navigate(R.id.action_profileFragment_to_subscriptionFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.aboutFragment){
                        navController.navigate(R.id.action_aboutFragment_to_subscriptionFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.configurationFragment){
                        navController.navigate(R.id.action_configurationFragment_to_subscriptionFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.permissionFragment){
                        navController.navigate(R.id.action_permissionFragment_to_subscriptionFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.contactFragment){
                        navController.navigate(R.id.action_contactFragment_to_subscriptionFragment)

                    }

                    else if (navController.currentDestination?.id==R.id.templateFragment){
                        navController.navigate(R.id.action_templateFragment_to_subscriptionFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.viewFragment){
                        navController.navigate(R.id.action_viewFragment_to_subscriptionFragment)

                    }
                }

                R.id.logout -> {
                    AppPreferences.isConfigured = false
                    AppPreferences.clearAllSharedPreferences(this)

                    val navController = findNavController(R.id.nav_host_fragment)
                    if (navController.currentDestination?.id==R.id.homeFragment) {
                        navController.navigate(R.id.action_homeFragment_to_sigInFragment)

                    }else if (navController.currentDestination?.id==R.id.profileFragment){
                        navController.navigate(R.id.action_profileFragment_to_sigInFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.aboutFragment){
                        navController.navigate(R.id.action_aboutFragment_to_sigInFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.templateFragment){
                        navController.navigate(R.id.action_templateFragment_to_sigInFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.configurationFragment){
                        navController.navigate(R.id.action_configurationFragment_to_sigInFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.subscriptionFragment){
                        navController.navigate(R.id.action_subscriptionFragment_to_sigInFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.permissionFragment){
                        navController.navigate(R.id.action_permissionFragment_to_sigInFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.contactFragment){
                        navController.navigate(R.id.action_contactFragment_to_sigInFragment)

                    }
                    else if (navController.currentDestination?.id==R.id.viewFragment){
                        navController.navigate(R.id.action_viewFragment_to_sigInFragment)

                    }
                }

                R.id.permission -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    if (navController.currentDestination?.id==R.id.homeFragment) {
                        navController.navigate(R.id.action_homeFragment_to_permissionFragment)

                    }else if (navController.currentDestination?.id==R.id.aboutFragment){
                        navController.navigate(R.id.action_aboutFragment_to_permissionFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.configurationFragment){
                        navController.navigate(R.id.action_configurationFragment_to_permissionFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.subscriptionFragment){
                        navController.navigate(R.id.action_subscriptionFragment_to_permissionFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.profileFragment){
                        navController.navigate(R.id.action_profileFragment_to_permissionFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.contactFragment){
                        navController.navigate(R.id.action_contactFragment_to_permissionFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.templateFragment){
                        navController.navigate(R.id.action_templateFragment_to_permissionFragment)
                    }
                    else if (navController.currentDestination?.id==R.id.viewFragment){
                        navController.navigate(R.id.action_viewFragment_to_permissionFragment)

                    }


                }


            }
            binding.drwerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun checkAppPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        ){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.PROCESS_OUTGOING_CALLS
                    ),
                    MY_PERMISSIONS_REQUEST_WRITE_FILES
                )
            }
        } else {
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FILES) {
            Log.e("TAG", "onRequestPermissionsResult: " + grantResults.size)
            val count = 0
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        } else {
            val builder =
                AlertDialog.Builder(this@MainActivity)
            builder.setMessage("App required some permission please enable it")
                .setPositiveButton("Yes") { dialog, id ->
                    // FIRE ZE MISSILES!
                    openPermissionScreen()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // User cancelled the dialog
                    dialog.dismiss()
                    finish()
                }
            dialog = builder.show()
        }
    }

    fun isAccessibilityEnabled(): Boolean {
        var accessibilityEnabled = 0
        val LIGHTFLOW_ACCESSIBILITY_SERVICE =
            "com.example.test/com.example.text.ccessibilityService"
        val accessibilityFound = false
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                this.getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
            Log.d(TAG, "ACCESSIBILITY: $accessibilityEnabled")
        } catch (e: Settings.SettingNotFoundException) {
            Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.message)
        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            Log.d(TAG, "***ACCESSIBILIY IS ENABLED***: ")
            val settingValue: String = Settings.Secure.getString(
                getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            Log.d(TAG, "Setting: $settingValue")
            if (settingValue != null) {
                val splitter: TextUtils.SimpleStringSplitter = mStringColonSplitter
                splitter.setString(settingValue)
                while (splitter.hasNext()) {
                    val accessabilityService: String = splitter.next()
                    Log.d(TAG, "Setting: $accessabilityService")
                    if (accessabilityService.equals("com.example.automatedwhatsappmessage", ignoreCase = true)
                    ) {
                        Log.d(
                            TAG,
                            "We've found the correct setting - accessibility is switched on!"
                        )
                        return true
                    }
                }
            }
            Log.d(TAG, "***END***")
        } else {
            Log.d(TAG, "***ACCESSIBILIY IS DISABLED***")
        }
        return accessibilityFound
    }


    fun openPermissionScreen(){
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this@MainActivity.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } //            public Map<String, String> getHeaders() {


    companion object{
        const val MY_PERMISSIONS_REQUEST_WRITE_FILES = 102
        const val TAG = "MainActivity"
    }

    // method to check is the user has permitted the accessibility permission
    // if not then prompt user to the system's Settings activity
    fun checkAccessibilityPermission(): Boolean {
        var accessEnabled = 0
        try {
            accessEnabled = Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }
        return if (accessEnabled == 0) {
            // if not construct intent to request permission
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // request permission via start activity for result
            startActivity(intent)
            false
        } else {
            true
        }
    }

    override fun onStart() {
        super.onStart()
        startService(Intent(applicationContext, CallDetectingService::class.java))


    }


    override fun onBackPressed() {
        if (binding.drwerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drwerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}