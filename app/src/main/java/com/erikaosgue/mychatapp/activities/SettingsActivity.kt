package com.erikaosgue.mychatapp.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.erikaosgue.mychatapp.R
import com.erikaosgue.mychatapp.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File


class SettingsActivity : AppCompatActivity() {


    var mDatabase = FirebaseDatabase.getInstance().reference
    var mCurrentUser = FirebaseAuth.getInstance().currentUser
    var mStorageRef = FirebaseStorage.getInstance().reference
    var GALLERY_ID: Int = 1
    lateinit var mContext : Context


    lateinit var actSettingsBinding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actSettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(actSettingsBinding.root)


        mContext = this

        val userId = mCurrentUser?.uid
        getUserData(userId.toString())
        setupUI()


    }


     /**
      * Updates the data that comes from the database into the view UI
      * @param userId id of the current user log In
      * @return true if purchase is consumed, false otherwise
     **/
    private fun getUserData(userId: String) {

        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)


        mDatabase.addValueEventListener(object: ValueEventListener {

            // snapshot is all the content from the user
            override fun onDataChange(snapshot: DataSnapshot) {

                val displayName = snapshot.child("display_name").value
                val image = snapshot.child("image").value.toString()
                val userStatus = snapshot.child("status").value
                val thumb = snapshot.child("thumb_image").value

                //Populate the information we got from database
                // to our views
                actSettingsBinding.settingsStatusId.text = userStatus.toString()
                actSettingsBinding.settingsDisplayNameId.text = displayName.toString()

                if (image != "default") {
                    Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.profile_img)
                        .into(actSettingsBinding.settingsProfileId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    //Register the settingsChangeStatus and the settingsChangeImage
    //buttons to listen onclick
    private fun setupUI() {

        actSettingsBinding.settingsChangeStatusBtn.setOnClickListener {

            val intent = Intent(this, StatusActivity::class.java).apply {
                putExtra("status", actSettingsBinding.settingsStatusId.text.toString().trim())
            }

            startActivity(intent)
        }

        actSettingsBinding.settingsChangeImgBtn.setOnClickListener{
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }

    }


    // Get the information as result from the Gallery, the
    // Idea is to create our picture file fro the data
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {
            //Crop Image
            cropImage(data?.data)
        }

        // Checking the result from the cropImage Activity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getActivityResult(data).uri
            compressImage(imageUri)
        }
        else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            val error = CropImage.getActivityResult(data).error
            Log.d("Error", error.toString())
        }
        else {
            Toast.makeText(this, "FAIL from cropImage!", Toast.LENGTH_LONG).show()
        }
    }



    // Crop the image that comes from the gallery
    private fun cropImage(image: Uri?) {

        /*Crop the Image from the library Image-Cropper:
        https://github.com/ArthurHub/Android-Image-Cropper

        It will start an activity that allow us to crop the image*/
        CropImage.activity(image)
            .setAspectRatio(1,1)
            .start(this)
    }

    private fun compressImage(imageUri: Uri) {

        val thumbFile = File(imageUri.path.toString())

        GlobalScope.launch {
            //compress the Image
            val thumbnail = Compressor.compress(mContext, thumbFile) {
                quality(100)
                format(Bitmap.CompressFormat.JPEG)
                default(width = 200, height = 200)
            }

            storeImage(imageUri, Uri.fromFile(thumbnail))
        }
    }

    private fun storeImage(imageUri: Uri, thumbFile: Uri) {

        val updateObj = HashMap<String, Any>()

        storeRefImage(imageUri, thumbFile)

    }
    private fun storeRefImage(imageUri: Uri, thumbFile: Uri) {

        val userId = mCurrentUser?.uid
        val imageFilePath = mStorageRef.child("profile_images").child("$userId.jpg")


        imageFilePath.putFile(imageUri).addOnSuccessListener { task ->
            // Pass the url where the image was store
            imageFilePath.downloadUrl.addOnSuccessListener { task ->

                val updateObj = HashMap<String, Any>()

                val imageUrl = task.toString()

                updateObj["image"] = imageUrl
                Log.d("image url =>", "$imageUrl")

                storeRefThumbnail(thumbFile, updateObj)
                //https://firebasestorage.googleapis.com/v0/b/mychatapp-d2b1c.appspot.com/o


            }.addOnFailureListener {
                showMessage("FAIL thumb and Image NOT Saved!")

            }
        }.addOnFailureListener {
            showMessage("FAIL Profile Image NOT Saved!")

        }
    }

    private fun storeRefThumbnail(thumbFile: Uri, updateObj: HashMap<String, Any> ) {

        val userId = mCurrentUser?.uid
        val thumbFilePath = mStorageRef.child("profile_images").child("thumbs").child("$userId.jpg")
        thumbFilePath.putFile(thumbFile).addOnSuccessListener {
            Log.d("Here understanding =>", "3")

            thumbFilePath.downloadUrl.addOnSuccessListener { task ->
                Log.d("Here understanding =>", "4")

                val thumbUrl = task.toString()

                updateObj["thumb_image"] = thumbUrl
                updateDatabase(updateObj)


            }.addOnFailureListener {
                showMessage("FAIL Profile Image NOT Saved!")

            }
        }.addOnFailureListener {
            showMessage("FAIL thumb and Image NOT Saved!")
        }
    }
    private fun updateDatabase(updateObj: HashMap<String, Any>) {
        Log.d("Here understanding =>", "5")

        //Add the url (from Storage) into the realtimeDatabase
        // Base on the current user
        mDatabase.updateChildren(updateObj).addOnSuccessListener {
            Log.d("Here understanding =>", "6")
            showMessage("Profile Image Saved!")

        }.addOnFailureListener {
            showMessage("Fail to Save Image in the Database")
        }

    }

    private fun showMessage(message: String)  {
        Toast.makeText(
            this, message,
            Toast.LENGTH_LONG)
            .show()

        Log.d("Here =>", message)

    }

}