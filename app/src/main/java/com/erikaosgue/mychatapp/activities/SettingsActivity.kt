package com.erikaosgue.mychatapp.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erikaosgue.mychatapp.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File


class SettingsActivity : AppCompatActivity() {


    var mDatabase : DatabaseReference?= null
    var mCurrentUser: FirebaseUser? = null
    var mStorageRef: StorageReference? = null
    var GALLERY_ID: Int = 1


    lateinit var actSettingsBinding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actSettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(actSettingsBinding.root)

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        mDatabase = FirebaseDatabase.getInstance().reference

        val userId = mCurrentUser?.uid
        updateUserData(userId.toString())
        setupUI()


    }

    private fun updateUserData(userId: String) {

        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)


        mDatabase?.addValueEventListener(object: ValueEventListener {

            // snapshot is all the content from the user
            override fun onDataChange(snapshot: DataSnapshot) {

                val displayName = snapshot.child("display_name").value
                val image = snapshot.child("image").value
                val userStatus = snapshot.child("status").value
                val thumb = snapshot.child("thumb_image").value

                //Populate the information we got from database
                // to our views
                actSettingsBinding.settingsStatusId.text = userStatus.toString()
                actSettingsBinding.settingsDisplayNameId.text = displayName.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {
            // get the image from the data intent
            val image: Uri? = data?.data


            //Comes from the cropImage library
            //Info: https://github.com/ArthurHub/Android-Image-Cropper

            //Will start an activity that allow us to crop the image
            CropImage.activity(image)
                .setAspectRatio(1,1)
                .start(this)
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri

                var userId = mCurrentUser?.uid
                val thumbFile = File(resultUri.path)


                compressImage(thumbFile, this)



            }
        }
        super.onActivityResult(requestCode, resultCode, data)


    }

    private fun compressImage(thumbFile: File, context: Context){
        GlobalScope.launch {

            //compress the Image
            val thumbBitmap = Compressor.compress(context, thumbFile)

            //upload the image and the thumbnail to firebase Storage
            //Download the ulr of the image and the thumbnail
            // create a new object to save the imageURL and the thumbnailURL into
            // the database
            //Questions?
            //1. Why should we save 2 images, into storage? thumbnail and image
            //2. Why is he compressing twice the image?
            //3. Why is he saving the image in one side and the thumbnail in another folder?

//            var byteArray = ByteArrayOutputStream()
//            thumbBitmap.compress( )
            println("here 1 $thumbBitmap")
        }
        println("here 2")
    }
}